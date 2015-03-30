package uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.client;

import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.config.AbstractWsConfig;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.model.GPMDBResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @user ypriverol
 */
public class GPMDBClient extends AbstractClient{
    /**
     * Default constructor for Archive clients
     *
     * @param config
     */
    public GPMDBClient(AbstractWsConfig config) {
        super(config);
    }

    /**
     * List of Peptides by Project Accession
     * @param sequence
     * @return
     * @throws java.io.IOException
     */
    public GPMDBResult getObservByChargeState(String sequence) throws IOException {

        Map<String, String> args = new HashMap<String, String>();

        String url = String.format("%s://%s/peptide/accessions/seq=%s?&format=json",
                config.getProtocol(), config.getHostName(), sequence);

        return this.restTemplate.getForObject(url, GPMDBResult.class, args);

    }

}
