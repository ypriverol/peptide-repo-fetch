package uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * user @ypriverol
 */
public class PRIDEClusterResult {

    @JsonProperty("charge")
    public Integer charge;                // the accession assigned to the assay,

    @JsonProperty("obsev")
    public Integer observs;                    // disease annotation provided for this assay (if applicable)

}
