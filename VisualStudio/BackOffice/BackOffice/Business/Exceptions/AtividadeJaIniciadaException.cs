using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackOffice.Business.Exceptions
{
    class AtividadeJaIniciadaException : Exception
    {
        public AtividadeJaIniciadaException()
        {
        }

        public AtividadeJaIniciadaException(string message)
        : base(message)
        {
        }

        public AtividadeJaIniciadaException(string message, Exception inner)
        : base(message, inner)
        {
        }
    }
}
