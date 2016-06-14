using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackOffice.Data.Json
{
    class StartConfig
    {
        public string email { get; set; }
        public string password { get; set; }
        public string port { get; set; }
        public string[] dicionario { get; set; }
        public string database { get; set; }
        public Simbolos simbolos { get; set; }

        public StartConfig(string mail, string pass, string porta, 
            string[] dic, Simbolos simb)
        {
            this.email = mail;
            this.password = pass;
            this.port = porta;
            this.dicionario = dic;
            this.simbolos = simb;
        }
    }
    class Simbolos
    {
        public Corner corner { get; set; }
        public Dictionary<String, String> road { get; set; }
        public Simbolos(Corner c, Dictionary<String, String> r)
        {
            this.corner = c;
            this.road = r;
        }
    }

    class Corner
    {
        public Dictionary<String, String> grade { get; set; }
        public Dictionary<String, String> duration { get; set; }
        public Dictionary<String, String> further { get; set; }
        public Corner(Dictionary<String, String> g, 
            Dictionary<String, String> d,
            Dictionary<String, String> f)
        {
            this.duration = d;
            this.further = f;
            this.grade = g;  
        }

    }
}
