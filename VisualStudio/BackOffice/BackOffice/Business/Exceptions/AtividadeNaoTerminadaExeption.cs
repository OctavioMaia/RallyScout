using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackOffice.Business.Exceptions
{
    class AtividadeNaoTerminadaExeption : Exception
    {
        public AtividadeNaoTerminadaExeption()
        {
        }

        public AtividadeNaoTerminadaExeption(string message)
        : base(message)
        {
        }

        public AtividadeNaoTerminadaExeption(string message, Exception inner)
        : base(message, inner)
        {
        }
    }
}
