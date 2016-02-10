package fr.univ_littoral.mpillier.testslideview2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

public class FragmentsMainActivity extends FragmentActivity {

    private PagerAdapter mPagerAdapter;
    private Socket socket;
    private static final int SERVERPORT = 6666;
    private static final String SERVER_IP = "10.0.2.2";
    InputStream stream = null;

    ImageView iv;
    Handler handlerTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.view_pager);

        // Création de la liste de Fragments que fera défiler le PagerAdapter
        List fragments = new Vector();

        // Ajout des Fragments dans la liste
        fragments.add(Fragment.instantiate(this, PageGaucheGaucheFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, PageGaucheFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, PageMilieuFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, PageDroiteFragment.class.getName()));

        // Création de l'adapter qui s'occupera de l'affichage de la liste de
        // Fragments
        this.mPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);

        ViewPager pager = (ViewPager) super.findViewById(R.id.viewpager);

        // Affectation de l'adapter au ViewPager
        pager.setAdapter(this.mPagerAdapter);

        iv = (ImageView) findViewById(R.id.imagePioche);

        new Thread(new ClientThread()).start();
    }

    /******************************************
     * DEBUT THREAD
     **************************************/
    class ClientThread implements Runnable {

        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

                socket = new Socket(serverAddr, SERVERPORT);

                System.out.println("socket client créée ! ");

                // le client donne son pseudo au serveur
                PrintWriter out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())),
                        true);
                out.println("clientTest");

                // mise sur écoute du client
                stream = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        stream));
                String line = "";
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Serveur dit : " + line);
                    if (line.contains("actionClicPiocheDonjon")) {

                        final String nomImage = line.split("-")[1];

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                test2(nomImage);
                            }
                        });
                    }
                }

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        public void test2(String nomImage) {

            System.out.println("nomImage : "+nomImage);

            iv = (ImageView) findViewById(R.id.imagePioche);

            iv.setImageResource(getResources().getIdentifier(nomImage, "drawable", getPackageName()));

        }
    }

    /******************************************
     * FIN THREAD
     **************************************/

    public void test(View v) {

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println("clicPiocheDonjon");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}