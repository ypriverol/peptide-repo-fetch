package uk.ac.ebi.pride.utilities.peptide_repo_fetcher.model;

import uk.ac.ebi.pride.utilities.util.Tuple;

import java.io.Serializable;
import java.util.*;

/**
 * Object to store protein related details
 * A Peptide will be a Tuple Protein ID + Sequence. The observations will be a number for every resource.
 * For example for gpmDB should be the number of observations in GPMDB, and in PRIDE Cluster will be the number
 * of reliable clusters that contain the specific protein and sequence. When the sequence is not found it in any resource
 * the value is 0 for the specific resource. If the sequence is not found it in any resource then the status is
 * UNKNOWN.
 *
 * <p/>
 * user: @ypriverol
 */
@SuppressWarnings("serial")
public class Peptide implements Serializable {

    public enum STATUS {UNKNOWN, HIGH, MEDIUM, POOR};

    public Tuple info;  // Peptide Sequence and Protein accession

    public Integer prideClusterObserv;

    public Integer gpmDBObsv;

    public STATUS status = STATUS.UNKNOWN;

    public Peptide(Tuple info){
        this.info = info;
    }

    public Peptide(Tuple info, Integer prideClusterObserv, STATUS status, Integer gpmDBObsv) {
        this.info = info;
        this.prideClusterObserv = prideClusterObserv;
        this.status = status;
        this.gpmDBObsv = gpmDBObsv;
    }

    public Tuple getInfo() {
        return info;
    }

    public void setInfo(Tuple info) {
        this.info = info;
    }

    public Integer getPrideClusterObserv() {
        return prideClusterObserv;
    }

    public void setPrideClusterObserv(Integer prideClusterObserv) {
        this.prideClusterObserv = prideClusterObserv;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public Integer getGpmDBObsv() {
        return gpmDBObsv;
    }

    public void setGpmDBObsv(Integer gpmDBObsv) {
        this.gpmDBObsv = gpmDBObsv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Peptide peptide = (Peptide) o;

        if (gpmDBObsv != null ? !gpmDBObsv.equals(peptide.gpmDBObsv) : peptide.gpmDBObsv != null) return false;
        if (!info.equals(peptide.info)) return false;
        if (prideClusterObserv != null ? !prideClusterObserv.equals(peptide.prideClusterObserv) : peptide.prideClusterObserv != null)
            return false;
        if (status != peptide.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = info.hashCode();
        result = 31 * result + (prideClusterObserv != null ? prideClusterObserv.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (gpmDBObsv != null ? gpmDBObsv.hashCode() : 0);
        return result;
    }
}
