package uk.ac.ebi.pride.utilities.peptide_repo_fetcher;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.model.Peptide;
import uk.ac.ebi.pride.utilities.util.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PeptideDetailFetcherTest {

    PeptideDetailFetcher detailFetcher = null;

    @Before
    public void setUp() throws Exception {
        detailFetcher = new PeptideDetailFetcher();

    }
    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetPeptideDetails() throws Exception {
        List<Tuple> sequences = new ArrayList<Tuple>();

        sequences.add(new Tuple("gi|6323379|","SPSSVEPVADMLMGLFFR"));
        sequences.add(new Tuple("sp|O75791|", "ILSNQEEWFK"));

        Map<Tuple, Peptide> peptides = detailFetcher.getPeptideDetails(sequences);

        for(Tuple sequence: peptides.keySet()){
            System.out.println(sequence + "\t" + "GPMDB Observations: " + peptides.get(sequence).getGpmDBObsv());
        }
    }
}