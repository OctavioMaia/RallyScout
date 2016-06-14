using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackOffice.Business
{
    class FichaBatedor
    {
        public int nAtividades { get; set; }
        public double horasEmReConhecimento { get; set; }

        public override string ToString()
        {
            return " (FichaBatedor) Atividades: " + nAtividades + " Horas: " +this.horasEmReConhecimento;
        }

        public FichaBatedor()
        {
            this.horasEmReConhecimento = 0;
            this.nAtividades = 0;
        }
        public FichaBatedor(int numAtividades, double horas)
        {
            this.horasEmReConhecimento = horas;
            this.nAtividades = numAtividades;
        }
        public void updateHoras(double time)
        {
            this.horasEmReConhecimento += time;
        }
        public void updateHorasAndActiv(double time)
        {
            this.horasEmReConhecimento += time;
            this.nAtividades++;
        }
    }
}
