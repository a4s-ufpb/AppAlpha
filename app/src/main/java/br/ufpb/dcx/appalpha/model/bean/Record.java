package br.ufpb.dcx.appalpha.model.bean;

/**
 * Entity Record
 */
public class Record implements Comparable<Record> {
    private String name;
    private double points;

    /**
     * Alloc instance with name and points
     *
     * @param nome
     * @param pontuacao
     */
    public Record(String nome, double pontuacao) {
        this.name = nome;
        this.points = pontuacao;
    }

    /**
     * Get name of Record
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Get points of Record
     *
     * @return
     */
    public double getPoints() {
        return points;
    }

    /**
     * Compare between object Record
     *
     * @param outroRecord
     * @return
     */
    @Override
    public int compareTo(Record outroRecord) {
        if (this.points > outroRecord.getPoints()) {
            return -1;
        }

        if (this.points < outroRecord.getPoints()) {
            return 1;
        }

        return 0;
    }
}
