using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackOffice.Data.Json
{
    public class JustToBack
    {
        public int idAtividade { get; set; }
        public string email { get; set; }
        public string password { get; set; }
        public Note[] notas { get; set; }
    }
    public class Note
    {
        public int idNota { get; set; }
        public string notaTextual { get; set; }
        public Cord local { get; set; }
        public string[] imagem { get; set; }
        public string audio { get; set; }
    }

}
