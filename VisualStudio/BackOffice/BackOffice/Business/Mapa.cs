using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Device.Location;
using System.Windows;

namespace BackOffice.Business
{using System.Xml.Linq;

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
            lerMAPFILE(path);
        }
        private void lerMAPFILE(string path)
        {
            LoadGPXWaypoints(path);
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
                GeoCoordinate g = new GeoCoordinate(Double.Parse(wpt.Latitude), Double.Parse(wpt.Longitude));
                this.cords.Add(i, g);
                i++;
            }

        }


        private string LoadGPXWaypointsS(string sFile)
        {
            XDocument gpxDoc = GetGpxDoc(sFile);
            XNamespace gpx = GetGpxNameSpace();

            var waypoints = from waypoint in gpxDoc.Descendants(gpx + "wpt")
                            select new
                            {
                                Latitude = waypoint.Attribute("lat").Value,
                                Longitude = waypoint.Attribute("lon").Value,
                                Elevation = waypoint.Element(gpx + "ele") != null ?
                                  waypoint.Element(gpx + "ele").Value : null,
                                Name = waypoint.Element(gpx + "name") != null ?
                                  waypoint.Element(gpx + "name").Value : null,
                                Dt = waypoint.Element(gpx + "cmt") != null ?
                                  waypoint.Element(gpx + "cmt").Value : null
                            };

            StringBuilder sb = new StringBuilder();
            int i = 0;
            foreach (var wpt in waypoints)
            {
                i++;
                // This is where we'd instantiate data 
                // containers for the information retrieved. 
                sb.Append(
                  string.Format("Name:{0} Latitude:{1} Longitude:{2} Elevation:{3} Date:{4}\n",
                  wpt.Name, wpt.Latitude, wpt.Longitude,
                  wpt.Elevation, wpt.Dt));
            }
            return sb.ToString();
        }
    }
}
