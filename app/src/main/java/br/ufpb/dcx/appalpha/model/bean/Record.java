package br.ufpb.dcx.appalpha.model.bean;

/**
 * Entity Record
 */
public class Record implements Comparable<Record>{
    private String nome;
    private double pontuacao;

    /**
     * Alloc instance with name and points
     * @param nome
     * @param pontuacao
     */
    public Record(String nome, double pontuacao) {
        this.nome = nome;
        this.pontuacao = pontuacao;
    }

    /**
     * Get name of Record
     * @return
     */
    public String getNome() {
        return nome;
    }

    /**
     * Get points of Record
     * @return
     */
    public double getPontuacao() {
        return pontuacao;
    }

    /**
     * Compare between object Record
     * @param outroRecord
     * @return
     */
    @Override
    public int compareTo(Record outroRecord) {
        if(this.pontuacao > outroRecord.getPontuacao()) {
            return -1;
        }

        if(this.pontuacao < outroRecord.getPontuacao()) {
            return 1;
        }

        return 0;
    }
}
