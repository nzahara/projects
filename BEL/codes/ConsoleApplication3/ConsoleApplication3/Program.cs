using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO.Ports;

public struct UartBufferData
{
    public System.UInt32 Device_id;
    public System.UInt32 sec;
    public System.UInt32 ms;
}
namespace ConsoleApplication3
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Serial comm");
            SendSampleData();
            Console.ReadKey();
        }



        private static void SendSampleData()
        {
            // Instantiate the communications
            // port with some basic settings
            SerialPort port = new SerialPort(
              "COM13", 115200, Parity.None, 8, StopBits.One);

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
                byte[] buffer = new byte[12];
                port.Read(buffer, 0, 12);
                UartBufferData buf;
                buf.Device_id = BitConverter.ToUInt32(buffer, 0);
                Console.WriteLine("{0:X}", buf.Device_id);
                //Console.WriteLine(buffer[0]);
                //Console.WriteLine(buffer[1]);
            }

        }

    }
}


//namespace ConsoleApplication3
//{
//    class Program
//    {
//         Create the serial port with basic settings
//        private SerialPort port = new SerialPort("COM3",
//          115200, Parity.None, 8, StopBits.One);

//        [STAThread]
//        static void Main(string[] args)
//        {
//             Instatiate this class
//            new Program();
//        }

//        private Program()
//        {
//            Console.WriteLine("Incoming Data:");

//             Attach a method to be called when there
//             is data waiting in the port's buffer
//            port.DataReceived += new
//              SerialDataReceivedEventHandler(port_DataReceived);

//             Begin communications
//            port.Open();

//             Enter an application loop to keep this thread alive
//            Application.Run();
//        }

//        private void port_DataReceived(object sender,
//          SerialDataReceivedEventArgs e)
//        {
//             Show all the incoming data in the port's buffer
//            Console.WriteLine(port.ReadExisting());
//        }
//    }
//}