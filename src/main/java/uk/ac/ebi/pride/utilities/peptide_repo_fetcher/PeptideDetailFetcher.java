package uk.ac.ebi.pride.utilities.peptide_repo_fetcher;

import java.io.IOException;
import java.util.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.pride.tools.utils.AccessionResolver;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.model.*;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.ProteinAccessionPattern;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.client.GPMDBClient;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.client.PRIDEClusterClient;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.config.AbstractWsConfig;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.config.GPMDBWsConfig;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.config.PRIDEClusterWsConfig;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.model.GPMDBResult;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.model.PRIDEClusterResult;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.model.PRIDEClusterResultList;
import uk.ac.ebi.pride.utilities.util.Tuple;

/**
 * This class retrieve for a Peptide (Peptide+ProteinID) the number of observations in different repositories such as
 * GPMDB and PRIDE Cluster. If the peptide is not founded, then the number of observations is 0, if the Peptide is not found
 * it in any repository the status is set to UNKNOWN.
 *
 *
 * @author ypriverol
 */
public class PeptideDetailFetcher {

    private static final Logger logger = LoggerFactory.getLogger(PeptideDetailFetcher.class);

    AbstractWsConfig gpmDBWSConfig;

    GPMDBClient gpmDBClient;

    AbstractWsConfig prideClusterWSConfig;

    PRIDEClusterClient prideClusterClient;

    PeptideDetailFetcher(){
        gpmDBWSConfig = new GPMDBWsConfig();
        prideClusterWSConfig = new PRIDEClusterWsConfig();
    }
    private HashMap<String, String> pageBuffer = new HashMap<String, String>();

    /**
     * Returns various details for the given protein (f.e. name,
     * sequence).
     * @param peptides for every peptide.
     * @return A Protein object containing the additional information.
     * @throws Exception error when retrieving protein accession
     */
    public Map<Tuple, Peptide> getPeptideDetails(Collection<Tuple> peptides) throws Exception {

        Map<Tuple, Peptide> rsPeptides = new HashMap<Tuple, Peptide>();

        rsPeptides.putAll(getClusterPeptideDetails(peptides));

        rsPeptides = addGPMDBInformation(rsPeptides);
    	
    	// add empty protein objects for all proteins that could not be retrieved
    	// and set the status to DELETED
    	for (Tuple tuple : peptides) {
    		if (!rsPeptides.containsKey(tuple)) {
    			Peptide p = new Peptide(tuple);
    			p.setStatus(Peptide.STATUS.UNKNOWN);
    			rsPeptides.put(tuple, p);
    		}
    	}
        return rsPeptides;
    }

    private Map<Tuple, Peptide> addGPMDBInformation(Map<Tuple, Peptide> peptides) throws IOException {

        gpmDBClient = new GPMDBClient(gpmDBWSConfig);
        Map<Tuple, Peptide> resultPeptides = new HashMap<Tuple, Peptide>(peptides.size());
        for(Tuple tuple: peptides.keySet()){
            String sequence = (String) tuple.getValue();
            GPMDBResult result = gpmDBClient.getObservByProtein(sequence);
            Peptide peptide = peptides.get(tuple);
            Integer gpmObservation = 0;
            if(result != null){
                /**
                 * Right now GPMDB do not have any mapping system, at least in the web-services, we will use the more conservative
                 * approach by looking inside the GPMDB ids if it contains the Id of the protein. We will clean the Protein iD to remove the
                 * prefix sp or gi from the ids. We will not work for the first release with isoforms.
                 */
                String proteinID = (String) tuple.getKey();
                proteinID = accessionResolver(proteinID);
                for(String resultId: result.observations.keySet()){
                    if(resultId.contains(proteinID)){
                       gpmObservation = result.observations.get(resultId);
                    }
                }
            }
            peptide.setGpmDBObsv(gpmObservation);
            resultPeptides.put(tuple, peptide);
        }
        return resultPeptides;
    }

    private String accessionResolver(String proteinID) {
        AccessionResolver accessionResolver = new AccessionResolver(proteinID, null);
        if(accessionResolver.isValidAccession()){
            proteinID = accessionResolver.getAccession();
        }
        return proteinID;
    }

    private Map<Tuple, Peptide> getClusterPeptideDetails(Collection<Tuple> sequences) throws Exception {
    	// build the query string for the accessions

        prideClusterClient = new PRIDEClusterClient(prideClusterWSConfig);
        Map<Tuple, Peptide> resultPeptides = new HashMap<Tuple, Peptide>(sequences.size());
        for(Tuple tuple: sequences){
            String sequence = (String) tuple.getKey();
            if(sequence != null && sequence.length() > 0){
                PRIDEClusterResultList result = prideClusterClient.getObservByChargeState(sequence);
                Map<Integer, Integer> prideClusterObserv = new HashMap<Integer, Integer>();
                if(result != null && result.list != null){
                    for(PRIDEClusterResult rs: result.list){
                        prideClusterObserv.put(rs.charge,rs.observs);
                    }
                }
                Peptide pep = new Peptide(tuple);
                pep.setPrideClusterObserv(1);
                resultPeptides.put(tuple, pep);
            }


        }
        return resultPeptides;
    }
}