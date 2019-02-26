using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO.Ports;


namespace TrafficConsole
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            SendSampleData();
            Application.Run(new Form1());
        }

        private static void SendSampleData()
        {
            // Instantiate the communications
            // port with some basic settings
            SerialPort port = new SerialPort(
                "COM3", 115200, Parity.None, 8, StopBits.One);

            // Open the port for communications
            port.Open();

            // Write a string
            //port.Write("Hello World");

            // Write a set of bytes
            //port.Write(new byte[] { 0x0A, 0xE2, 0xFF }, 0, 3);

            // Close the port
            //port.Close();
            while (true)
            {
                //byte[] buffer = new byte[2];
                Console.Write((char)port.ReadChar());
                //Console.WriteLine(buffer[0]);
                //Console.WriteLine(buffer[1]);
            }

        }
    }

    
}
