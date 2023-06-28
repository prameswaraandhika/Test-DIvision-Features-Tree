package model;

/**
 *
 * @author Andhika Prameswara <prameswaara@gmail.com>
 */
public class Divisi {

    private long id;
    private String divisi;
    private String kodeDivisi;

    public Divisi(long id, String divisi, String kodeDivisi) {
        this.id = id;
        this.divisi = divisi;
        this.kodeDivisi = kodeDivisi;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDivisi() {
        return divisi;
    }

    public void setDivisi(String divisi) {
        this.divisi = divisi;
    }

    public String getKodeDivisi() {
        return kodeDivisi;
    }

    public void setKodeDivisi(String kodeDivisi) {
        this.kodeDivisi = kodeDivisi;
    }

}
