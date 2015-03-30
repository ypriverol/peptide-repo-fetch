package uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.config;

/**
 * This class help to configure the web-service provider that would be used.
 */
public class GPMDBWsConfig extends AbstractWsConfig {

    public GPMDBWsConfig() {
        super("http", "rest.thegpm.org/1/");
    }
}
