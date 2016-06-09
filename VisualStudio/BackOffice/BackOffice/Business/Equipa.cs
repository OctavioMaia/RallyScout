using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackOffice.Business
{
    class Equipa
    {
        public string nome { get; set; }
        public string email { get; set; }
        public Equipa(string name, string mail)
        {
            this.nome = name;
            this.email = mail;

        }
        public override bool Equals(object obj)
        {
            if (obj == null)
            {
                return false;
            }
            Equipa e = obj as Equipa;
            if ((System.Object)e == null)
            {
                return false;
            }
            return this.email.Equals(e.email);
        }
    }
}
