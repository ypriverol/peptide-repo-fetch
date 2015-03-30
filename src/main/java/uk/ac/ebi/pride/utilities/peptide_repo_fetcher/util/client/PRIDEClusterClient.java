package uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.client;

import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.config.AbstractWsConfig;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.model.PRIDEClusterResultList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @user ypviverol
 */
public class PRIDEClusterClient extends AbstractClient{
    /**
     * Default constructor for Archive clients
     *
     * @param config
     */
    public PRIDEClusterClient(AbstractWsConfig config) {
        super(config);
    }

    /**
     * List of Peptides by Project Accession
     * @param sequence
     * @return
     * @throws java.io.IOException
     */
    public PRIDEClusterResultList getObservByChargeState(String sequence) throws IOException {

        Map<String, String> args = new HashMap<String, String>();

        String url = String.format("%s://%s/pride/ws/cluster/cluster/search?peptide=%s",
                config.getProtocol(), config.getHostName(), sequence);

        return this.restTemplate.getForObject(url, PRIDEClusterResultList.class, args);

    }

}
