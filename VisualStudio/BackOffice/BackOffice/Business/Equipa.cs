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
    }
}
