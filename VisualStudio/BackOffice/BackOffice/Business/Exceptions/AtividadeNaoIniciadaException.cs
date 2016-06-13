using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackOffice.Business.Exceptions
{
    class AtividadeNaoIniciadaException : Exception
    {
        public AtividadeNaoIniciadaException()
        {
        }

        public AtividadeNaoIniciadaException(string message)
        : base(message)
        {
        }

        public AtividadeNaoIniciadaException(string message, Exception inner)
        : base(message, inner)
        {
        }
    }
}
