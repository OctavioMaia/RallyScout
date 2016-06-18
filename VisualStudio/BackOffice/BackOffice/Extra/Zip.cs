using System;
using System.Collections.Generic;
using System.IO;
using System.IO.Compression;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackOffice.Extra
{
    class Zip
    {



        private static byte[] zip(string str)
        {
            var bytes = Encoding.UTF8.GetBytes(str);

            using (var msi = new MemoryStream(bytes))
            using (var mso = new MemoryStream())
            {
                using (var gs = new GZipStream(mso, CompressionMode.Compress))
                {
                    //msi.CopyTo(gs);
                    CopyTo(msi, gs);
                }

                return mso.ToArray();
            }
        }

        private static string Unzip(byte[] bytes)
        {
            using (var msi = new MemoryStream(bytes))
            using (var mso = new MemoryStream())
            {
                using (var gs = new GZipStream(msi, CompressionMode.Decompress))
                {
                    //gs.CopyTo(mso);
                    CopyTo(gs, mso);
                }

                return Encoding.UTF8.GetString(mso.ToArray());
            }
        }

        private static void CopyTo(Stream src, Stream dest)
        {
            byte[] bytes = new byte[4096];

            int cnt;

            while ((cnt = src.Read(bytes, 0, bytes.Length)) != 0)
            {
                dest.Write(bytes, 0, cnt);
            }
        }

        private static string fromBytes64(byte[] bytes)
        {
            string ret = Convert.ToBase64String(bytes);
            return ret;
        }

        private static byte[] toBytes64(string s)
        {
            byte[] bytes = Convert.FromBase64String(s);
            return bytes;
        }

        public static string zipStringBase64(string str)
        {
            byte[] zipB = Zip.zip(str);
            string rep64zip = Zip.fromBytes64(zipB);
            return rep64zip;

        }

        public static string UnzipStringBase64(string rep64zip)
        {
            byte[] zipado = Zip.toBytes64(rep64zip);
            string original = Zip.Unzip(zipado);
            return original;
        }
    }
}
