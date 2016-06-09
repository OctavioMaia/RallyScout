using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackOffice.Business
{
    class Veiculo
    {
        public string modelo { get; set; }
        public string marca { get; set; }
        public string chassi { get; set; }
        public List<string> caracteristicas { get; set; }


        public Veiculo(string mod, string marc, string chassie, List<string> carac)
        {
            this.modelo = mod;
            this.marca = marc;
            this.chassi = chassie;
            this.caracteristicas = carac;  
         }

        public Veiculo(string mod, string marc, string chassie)
        {
            this.modelo = mod;
            this.marca = marc;
            this.chassi = chassie;
            this.caracteristicas = new List<string>();
        }

        public void addCaract(string carcteristica)
        {
            this.caracteristicas.Add(carcteristica);
        }

        public override bool Equals(object obj)
        {
            if (obj == null)
            {
                return false;
            }
            Veiculo v = obj as Veiculo;
            if ((System.Object)v == null)
            {
                return false;
            }
            return this.chassi.Equals(v.chassi);
        }

    }
}
