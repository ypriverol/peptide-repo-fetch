package uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.config;

/**
 * @author jadianes
 * @author ypriverol
 *
 */
public abstract class AbstractWsConfig {

    private String hostName;
    private String protocol;

    protected AbstractWsConfig(String protocol, String hostName) {
        this.hostName = hostName;
        this.protocol = protocol;
    }

    public String getHostName() {
        return hostName;
    }


    public String getProtocol() {
        return protocol;
    }

}
