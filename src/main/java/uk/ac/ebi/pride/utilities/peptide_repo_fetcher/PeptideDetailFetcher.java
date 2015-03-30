package uk.ac.ebi.pride.utilities.peptide_repo_fetcher;

import java.io.IOException;
import java.util.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.model.*;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.client.GPMDBClient;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.client.PRIDEClusterClient;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.config.AbstractWsConfig;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.config.GPMDBWsConfig;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.config.PRIDEClusterWsConfig;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.model.GPMDBResult;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.model.PRIDEClusterResult;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.model.PRIDEClusterResultList;
import uk.ac.ebi.pride.utilities.util.Tuple;

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
     * @param sequences for every peptide.
     * @return A Protein object containing the additional information.
     * @throws Exception error when retrieving protein accession
     */
    public Map<Tuple, Peptide> getPeptideDetails(Collection<Tuple> sequences) throws Exception {

        Map<Tuple, Peptide> peptides = new HashMap<Tuple, Peptide>();

        peptides.putAll(getClusterPeptideDetails(sequences));

        peptides = addGPMDBInformation(peptides);
    	
    	// add empty protein objects for all proteins that could not be retrieved
    	// and set the status to DELETED
    	for (Tuple tuple : sequences) {
    		if (!peptides.containsKey(tuple)) {
    			Peptide p = new Peptide(tuple);
    			p.setStatus(Peptide.STATUS.UNKNOWN);
    			peptides.put(tuple, p);
    		}
    	}
        return peptides;
    }

    private Map<Tuple, Peptide> addGPMDBInformation(Map<Tuple, Peptide> peptides) throws IOException {

        gpmDBClient = new GPMDBClient(gpmDBWSConfig);
        Map<Tuple, Peptide> resultPeptides = new HashMap<Tuple, Peptide>(peptides.size());
        for(Tuple tuple: peptides.keySet()){
            String sequence = (String) tuple.getValue();
            GPMDBResult result = gpmDBClient.getObservByProtein(sequence);
            Peptide peptide = peptides.get(sequence);
            Map<Integer, Integer> gpmdbOvs = new HashMap<Integer, Integer>();
            if(result != null){
                if(result.observations.containsKey((String)tuple.getKey()));
            }
            peptide.setGpmDBObsv(1);
            resultPeptides.put(tuple, peptide);
        }
        return resultPeptides;
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