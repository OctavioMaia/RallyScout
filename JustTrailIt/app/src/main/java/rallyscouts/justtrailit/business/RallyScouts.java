package rallyscouts.justtrailit.business;

/**
 * Created by rjaf on 09/06/16.
 */
public class RallyScouts {
    private String batedorEmail;
    private String password;
    private Atividade atividade;

    public RallyScouts() { }

    public RallyScouts(String batedorEmail, String password) {
        this.batedorEmail = batedorEmail;
        this.password = password;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public String getBatedorEmail() {
        return batedorEmail;
    }

    public void setBatedorEmail(String batedorEmail) {
        this.batedorEmail = batedorEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RallyScouts{" +
                "atividade=" + atividade +
                ", batedorEmail='" + batedorEmail + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
