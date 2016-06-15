using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackOffice.Business.Exceptions
{
    class NaoExisteBatedorExeption : Exception
    {
        public NaoExisteBatedorExeption()
        {
        }

        public NaoExisteBatedorExeption(string message)
        : base(message)
        {
        }

        public NaoExisteBatedorExeption(string message, Exception inner)
        : base(message, inner)
        {
        }
    }
}
