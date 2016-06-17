using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackOffice.Business.Exceptions
{
    class MapaVazioException : Exception
    {
        public MapaVazioException()
        {
        }

        public MapaVazioException(string message)
        : base(message)
        {
        }

        public MapaVazioException(string message, Exception inner)
        : base(message, inner)
        {
        }
    }
}
