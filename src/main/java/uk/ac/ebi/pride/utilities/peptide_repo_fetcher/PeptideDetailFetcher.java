package uk.ac.ebi.pride.utilities.peptide_repo_fetcher;

import java.io.IOException;
import java.util.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

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

        Map<String, Peptide> peptides = new HashMap<String, Peptide>();

//        for (Tuple sequence : sequences) {
//    		// Validate the sequences
//    		if(sequence != null && ((String)sequence.getValue()).length() > 0) {
//                validatedSequences.add(sequence);
//            }
//        }

        peptides.putAll(getClusterPeptideDetails(sequences));

        peptides = addGPMDBInformation(peptides);
    	
    	// add empty protein objects for all proteins that could not be retrieved
    	// and set the status to DELETED
    	for (String accession : sequences) {
    		if (!peptides.containsKey(accession)) {
    			Peptide p = new Peptide(accession);
    			p.setStatus(Peptide.STATUS.UNKNOWN);
    			peptides.put(accession, p);
    		}
    	}
        return peptides;
    }

    private Map<String, Peptide> addGPMDBInformation(Map<String, Peptide> peptides) throws IOException {

        gpmDBClient = new GPMDBClient(gpmDBWSConfig);
        Map<String, Peptide> resultPeptides = new HashMap<String, Peptide>(peptides.size());
        for(String sequence: peptides.keySet()){
            GPMDBResult result = gpmDBClient.getObservByChargeState(sequence);
            Peptide peptide = peptides.get(sequence);
            Map<Integer, Integer> gpmdbOvs = new HashMap<Integer, Integer>();
            if(result != null){
                gpmdbOvs.put(1, result.singleCharged);
                gpmdbOvs.put(2, result.doubleCharged);
                gpmdbOvs.put(3, result.tripleChargerd);
                gpmdbOvs.put(4, result.fourCharge);
                gpmdbOvs.put(5, result.fiveCharge);
            }
            peptide.setGpmDBObsv(gpmdbOvs);
            resultPeptides.put(sequence, peptide);
        }
        return resultPeptides;
    }



    private Map<String, Peptide> getClusterPeptideDetails(Collection<Tuple> sequences) throws Exception {
    	// build the query string for the accessions


        prideClusterClient = new PRIDEClusterClient(prideClusterWSConfig);
        Map<String, Peptide> resultPeptides = new HashMap<String, Peptide>(sequences.size());
        for(Tuple sequence: sequences){
            PRIDEClusterResultList result = prideClusterClient.getObservByChargeState(sequence);
            Map<Integer, Integer> prideClusterObserv = new HashMap<Integer, Integer>();
            if(result != null && result.list != null){
               for(PRIDEClusterResult rs: result.list){
                   prideClusterObserv.put(rs.charge,rs.observs);
               }
            }
            Peptide pep = new Peptide(sequence);
            pep.setPrideClusterObserv(prideClusterObserv);
            resultPeptides.put(sequence, pep);
        }
        return resultPeptides;
    }
}