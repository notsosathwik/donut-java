package donutJava;

import java.util.Arrays;

public class donut {
    char[] buffer;
    int height;
    int width;
    final static String newLine = "\n";
    final static double phi_step = 0.07;
    final static double theta_step = 0.02;
    final static char[] shade_chars = {'.',',','-','~',':',';','=','!','*','#','$','@'};

    final static double TWO_PI = 6.28;

    public donut(char[] buffer, int height, int width) {
        this.buffer = buffer;
        this.height = height;
        this.width = width;
    }

    private static void display_buffer(donut canvas){
        int buffer_size = canvas.height * canvas.width;
        System.out.print("\u001b[2H"); // this line moves cursor to the top
        for(int k=0; buffer_size >= k; k++){
            if(k % canvas.width > 0){
                System.out.print(canvas.buffer[k]);
            }else{
                System.out.print(newLine);
            }
        }
    }

    private static void iterate_buffer(donut canvas, double[] z, double A, double B){
        double outer_size = canvas.width/40.0;
        int buffer_size = canvas.height * canvas.width;
        Arrays.fill(canvas.buffer,0,buffer_size,' ');
        Arrays.fill(z,0,buffer_size,0);
        for(double phi = 0; phi < TWO_PI; phi += phi_step){
            for(double theta = 0; theta < TWO_PI; theta += theta_step){
                double sin_theta = Math.sin(theta);
                double cos_phi = Math.cos(phi);
                double sin_A = Math.sin(A);
                double sin_phi = Math.sin(phi);
                double cos_A = Math.cos(A);
                double h = cos_phi + outer_size;
                double D = 1 / (sin_theta*h*sin_A*sin_phi*cos_A + 5);
                double cos_theta = Math.cos(theta);
                double cos_B = Math.cos(B);
                double sin_B = Math.sin(B);
                double t = sin_theta * h * cos_A - sin_phi * sin_A;
                int x = (int) ((canvas.width / 2) + 30 * D * (cos_theta * h * cos_B - t * sin_B));
                int y = (int) ((canvas.height / 2 + 1) + 15 * D * (cos_theta * h * sin_B + t * cos_B));
                int o = x + canvas.width * y;
                int N = (int) (8 * ((sin_phi * sin_A - sin_theta * cos_phi * cos_A) * cos_B - sin_theta * cos_phi * sin_A - sin_phi * cos_A - cos_theta * cos_phi * sin_B));

                if(canvas.height > y && y > 0 && x > 0 && canvas.width > x && D > z[o]){ 
                    z[o] = D;
                    int num = Math.max(N, 0);
                    canvas.buffer[o] = shade_chars[num];
                }
            }
        }
    }

    public static void main(String[] args) {
        double A = 0;
        double B = 0;
        int width = 80;
        int height = 22;
        int buffer_size = height * width;
        double[] z = new double[buffer_size];
        char[] buffer = new char[buffer_size];
        donut canvas = new donut(buffer,height,width);
        System.out.print("\\u001b[J"); // this line clears the console
        while(true){
            iterate_buffer(canvas,z,A,B);
            display_buffer(canvas);
            A += 0.04;
            B += 0.02;
        }
    }
}
