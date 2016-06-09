using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackOffice.Business
{
    class VeiculoRepetidoException :Exception
    {
        public VeiculoRepetidoException()
        {
        }

        public VeiculoRepetidoException(string message)
        : base(message)
    {
        }

        public VeiculoRepetidoException(string message, Exception inner)
        : base(message, inner)
    {
        }
    }
}
