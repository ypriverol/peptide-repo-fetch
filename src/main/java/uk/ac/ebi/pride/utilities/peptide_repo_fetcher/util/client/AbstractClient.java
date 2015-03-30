package uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.client;

import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.config.AbstractWsConfig;

/**
 * @author ypriverol
 */
public class AbstractClient {

    protected RestTemplate restTemplate;
    protected AbstractWsConfig config;

    /**
     * Default constructor for Archive clients
     * @param config
     */
    public AbstractClient(AbstractWsConfig config){
        this.config = config;
        this.restTemplate = new RestTemplate();
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AbstractWsConfig getConfig() {
        return config;
    }

    public void setConfig(AbstractWsConfig config) {
        this.config = config;
    }
}
