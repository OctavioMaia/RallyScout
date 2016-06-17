using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackOffice.Business
{
    public class Batedor
    {
        public string email { get; set; }
        public string nome { get; set; }
        public string password { get; set; }
        public FichaBatedor ficha { get; set; }

        public override bool Equals(object obj)
        {
            Batedor b = obj as Batedor;
            if (b == null)
                return false;
            else
                return this.email.Equals(b.email);

        }

        public override string ToString()
        {
            return " (Batedor) Email: " + email + " Nome: " + nome+" pass: " + password + this.ficha.ToString();
        }

        
        public Batedor(string mail, string name, string pass)
        {
            this.nome = name;
            this.email = mail;
            this.password = pass;
            this.ficha = new FichaBatedor();
        }


        public Batedor(string mail, string name, string pass, int numAtiv, double horasAtiv)
        {
            this.nome = name;
            this.email = mail;
            this.password = pass;
            this.ficha = new FichaBatedor(numAtiv, horasAtiv);
        }

        public Batedor(string mail, string name, string pass, FichaBatedor ficha)
        {
            this.nome = name;
            this.email = mail;
            this.password = pass;
            this.ficha = ficha;
        }

        public void updateHoras(double time)
        {
            this.ficha.updateHoras(time);
        }

        public void updateHorasAndActiv(double time)
        {
            this.ficha.updateHorasAndActiv(time);
        }


        public int getTotAtiv()
        {
            return this.ficha.nAtividades;
        }
        public double getWorkTime()
        {
            return this.ficha.horasEmReConhecimento;
        }
    }
}
