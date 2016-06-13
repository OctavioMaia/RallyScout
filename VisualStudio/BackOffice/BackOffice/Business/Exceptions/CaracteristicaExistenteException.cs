using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackOffice.Business.Exceptions
{
    class CaracteristicaExistenteException : Exception
    {
        public CaracteristicaExistenteException()
        {
        }

        public CaracteristicaExistenteException(string message)
        : base(message)
        {
        }

        public CaracteristicaExistenteException(string message, Exception inner)
        : base(message, inner)
        {
        }
    }
}
