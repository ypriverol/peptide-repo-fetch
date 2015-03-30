package uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * @user ypriverol
 */

@JsonIgnoreProperties(ignoreUnknown = true)

public class GPMDBResult {

//    @JsonProperty("1")
//    public Integer singleCharged;              // the accession assigned to the assay,
//
//    @JsonProperty("2")
//    public Integer doubleCharged;              // disease annotation provided for this assay (if applicable)
//
//    @JsonProperty("3")
//    public int tripleChargerd;                 // number of proteins in this assay
//
//    @JsonProperty("4")
//    public int fourCharge;                     //number of peptides in this assay
//
//    @JsonProperty("5")
//    public int fiveCharge;                     // number of unique peptides in this assay

    public Map<String, Integer> observations;    // Map a set of properties such as PROTEINID -> NUMBER OF OBSERVATIONS

}
