package uk.ac.ebi.pride.utilities.peptide_repo_fetcher;

import junit.framework.TestCase;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.model.Peptide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PeptideDetailFetcherTest extends TestCase {

    PeptideDetailFetcher detailFetcher = null;

    public void setUp() throws Exception {
        detailFetcher = new PeptideDetailFetcher();

    }

    public void tearDown() throws Exception {

    }

    public void testGetPeptideDetails() throws Exception {
        List<String> sequences = new ArrayList<String>();
        sequences.add("SPSSVEPVADMLMGLFFR");

        Map<String, Peptide> peptides = detailFetcher.getPeptideDetails(sequences);

        for(String sequence: peptides.keySet()){
            System.out.println(sequence + "\t" + "gpmdObserv charge 2: " + peptides.get(sequence).gpmObservationsByCharge(2));
        }
    }
}