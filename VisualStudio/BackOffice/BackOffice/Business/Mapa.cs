using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Device.Location;
using System.Windows;

namespace BackOffice.Business
{
    using Exceptions;
    using System.Globalization;
    using System.Xml.Linq;

    public class Mapa
    {
        public string nomeProva { get; set; }
        public int idMapa { get; set; }
        public Dictionary<int, GeoCoordinate> cords { get; set; } 


        public Mapa(string nome, int id, string path)
        {
            this.nomeProva = nome;
            this.idMapa = id;
            this.cords = new Dictionary<int, GeoCoordinate>();
            lerMAPFILE(path);
        }

        public Mapa(string nome, int id, Dictionary<int, GeoCoordinate> d)
        {
            this.nomeProva = nome;
            this.idMapa = id;
            this.cords = d;
        }
        private void lerMAPFILE(string path)
        {
            LoadGPXWaypoints(path);
            if (this.cords == null || this.cords.Count==0)
            {
                throw new MapaVazioException("Não foi possível carregar as coordenadas do " + path);
            }
        }


        private XDocument GetGpxDoc(string sFile)
        {
            XDocument gpxDoc = XDocument.Load(sFile);
            return gpxDoc;
        }
        private XNamespace GetGpxNameSpace()
        {
            XNamespace gpx = XNamespace.Get("http://www.topografix.com/GPX/1/1");
            return gpx;
        }


        private void LoadGPXWaypoints(string sFile)
        {
            XDocument gpxDoc = GetGpxDoc(sFile);
            XNamespace gpx = GetGpxNameSpace();

            var waypoints = from waypoint in gpxDoc.Descendants(gpx + "trkpt")
                            select new
                            {
                                Latitude = waypoint.Attribute("lat").Value,
                                Longitude = waypoint.Attribute("lon").Value,
                            };
            int i = 0;
            foreach (var wpt in waypoints)
            {
                Double lat = Double.Parse(wpt.Latitude,CultureInfo.InvariantCulture);
                Double longit = Double.Parse(wpt.Longitude, CultureInfo.InvariantCulture);


                GeoCoordinate g = new GeoCoordinate(lat, longit);
                this.cords.Add(i, g);
                i++;
            }

        }

    }
}
