using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Device.Location;

namespace BackOffice.Business
{
    class Mapa
    {
        public string nomeProva { get; set; }
        public int idMapa { get; set; }
        public Dictionary<int, GeoCoordinate> cords { get; set; } 


        public Mapa(string nome, int id, string path)
        {
            this.nomeProva = nome;
            this.idMapa = id;
            this.cords = new Dictionary<int, GeoCoordinate>();
            lerMAPFILE();
        }
        private void lerMAPFILE()
        {
            //TODO fazer quando souber a contedo do ficheiro do mapa
        }
    }
}
