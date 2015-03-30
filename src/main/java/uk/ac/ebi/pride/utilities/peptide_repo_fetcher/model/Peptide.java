package uk.ac.ebi.pride.utilities.peptide_repo_fetcher.model;

import java.io.Serializable;
import java.util.*;

/**
 * Object to store protein related details
 * <p/>
 * user: @ypriverol
 */
@SuppressWarnings("serial")
public class Peptide implements Serializable {

    public enum STATUS {UNKNOWN, HIGH, MEDIUM, POOR, ERROR};

    public String sequence;

    public Map<Integer, Integer> prideClusterObserv;

    public STATUS status;

    public Map<Integer, Integer> gpmDBObsv;

    public Peptide(String sequence){
        this.sequence = sequence;
    }

    public String getSequence() {
        return sequence;
    }

    public Peptide(String sequence, Map<Integer, Integer> prideClusterObserv, Map<Integer, Double> qualityScore, Map<Integer, Double> massQualityScore, STATUS status, Map<Integer, Integer> gpmDBObsv) {
        this.sequence = sequence;
        this.prideClusterObserv = prideClusterObserv;
        this.status = status;
        this.gpmDBObsv = gpmDBObsv;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Map<Integer, Integer> getPrideClusterObserv() {
        return prideClusterObserv;
    }

    public void setPrideClusterObserv(Map<Integer, Integer> prideClusterObserv) {
        this.prideClusterObserv = prideClusterObserv;
    }

    public Integer gpmObservationsByCharge(Integer charge){
        return gpmDBObsv.get(charge);
    }

    public Integer prideClusterObservationsByCharge(Integer charge){
        return prideClusterObserv.get(charge);
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public Map<Integer, Integer> getGpmDBObsv() {
        return gpmDBObsv;
    }

    public void setGpmDBObsv(Map<Integer, Integer> gpmDBObsv) {
        this.gpmDBObsv = gpmDBObsv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Peptide peptide = (Peptide) o;

        if (gpmDBObsv != null ? !gpmDBObsv.equals(peptide.gpmDBObsv) : peptide.gpmDBObsv != null) return false;
        if (prideClusterObserv != null ? !prideClusterObserv.equals(peptide.prideClusterObserv) : peptide.prideClusterObserv != null)
            return false;
        if (!sequence.equals(peptide.sequence)) return false;
        if (status != peptide.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sequence.hashCode();
        result = 31 * result + (prideClusterObserv != null ? prideClusterObserv.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (gpmDBObsv != null ? gpmDBObsv.hashCode() : 0);
        return result;
    }
}
