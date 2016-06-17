using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackOffice.Business.Exceptions
{
    class NaoExisteAtividadeExeption : Exception
    {
        public NaoExisteAtividadeExeption()
        {
        }

        public NaoExisteAtividadeExeption(string message)
        : base(message)
        {
        }

        public NaoExisteAtividadeExeption(string message, Exception inner)
        : base(message, inner)
        {
        }
    }
}
