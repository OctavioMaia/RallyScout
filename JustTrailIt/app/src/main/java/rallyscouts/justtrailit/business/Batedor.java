package rallyscouts.justtrailit.business;

/**
 * Created by rjaf on 10/06/16.
 */
public class Batedor {
    private String email;
    private String nome;
    private String password;
    private int atividade;

    public Batedor(String email, String nome, String password, int atividade) {
        this.email = email;
        this.nome = nome;
        this.password = password;
        this.atividade = atividade;
    }

    public int getAtividade() {
        return atividade;
    }

    public void setAtividade(int atividade) {
        this.atividade = atividade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
