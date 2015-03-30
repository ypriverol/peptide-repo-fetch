package uk.ac.ebi.pride.utilities.peptide_repo_fetcher.util.model;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * This class retrieve the number of observations for a peptide sequence for a set of Proteins
 * @user ypriverol
 */

@JsonIgnoreProperties(ignoreUnknown = true)

public class GPMDBResult {

    public Map<String, Integer> observations = new HashMap<String, Integer>();    // Map a set of properties such as PROTEINID -> NUMBER OF OBSERVATIONS

    @JsonAnyGetter
    public Map<String, Integer> any() {
        return observations;
    }

    @JsonAnySetter
    public void set(String name, Integer value) {
        observations.put(name, value);
    }

    public boolean hasUnknowProperties() {
        return !observations.isEmpty();
    }
}
