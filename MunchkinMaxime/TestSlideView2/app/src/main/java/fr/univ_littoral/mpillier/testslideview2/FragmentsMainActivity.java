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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentsMainActivity extends FragmentActivity {

    private PagerAdapter mPagerAdapter;
    private Socket socket;
    private static final int SERVERPORT = 6666;
    private static final String SERVER_IP = "10.0.2.2";
    InputStream stream = null;

    ImageView ivPioche;
    TextView tvInfoBulle;
    TextView tvNomJoueur;
    TextView tvNomJoueurCombat = null;
    String nomJoueur = null;

    Handler handlerTest;

    List fragments = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.view_pager);

        // Création de la liste de Fragments que fera défiler le PagerAdapter
        fragments = new Vector();

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

        // page par défaut
        pager.setCurrentItem(2);

        ivPioche = (ImageView) findViewById(R.id.imagePioche);

        new Thread(new ClientThread(this)).start();
    }

    /******************************************
     * DEBUT THREAD
     **************************************/
    class ClientThread implements Runnable {

        FragmentsMainActivity activity = null;

        public ClientThread(FragmentsMainActivity activity) {

            this.activity = activity;
        }

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

                        actionClicPiocheDonjon(line);

                    }

                    if (line.contains("afficherNomJoueur")) {

                        afficherNomJoueur(line);

                    }

                }

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        // action liée à un clic sur la pioche donjon
        public void actionClicPiocheDonjon(final String line) {

            final String nomJoueur = line.split("-")[1];
            final String levelJoueur = line.split("-")[2];
            final String attaqueJoueur = line.split("-")[3];
            final String nomImage = line.split("-")[4];
            final String typeCarte = line.split("-")[5];

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    afficherCartePiochee(nomImage);
                    afficherInfosCartePiochee(nomJoueur, nomImage);

                    if (typeCarte.equals("monstre")) {

                        final String levelMonstre = line.split("-")[6];
                        final String ataqueMonstre = line.split("-")[7];

                        combattre(nomJoueur, levelJoueur, attaqueJoueur, nomImage, levelMonstre, ataqueMonstre);
                    }

                }
            });

        }

        // affichage des infos dans l'infobulle
        public void afficherInfosCartePiochee(String nomJoueur, String nomImage) {

            tvInfoBulle = (TextView) findViewById(R.id.infoBulle);
            // on rend visible l'infobulle
            tvInfoBulle.setVisibility(View.VISIBLE);
            tvInfoBulle.setText(nomJoueur + " a pioché la carte " + nomImage);
        }

        // affichage de la carte piochée
        public void afficherCartePiochee(String nomImage) {

            ivPioche = (ImageView) findViewById(R.id.imagePioche);
            ivPioche.setImageResource(getResources().getIdentifier(nomImage, "drawable", getPackageName()));
        }

        // actions liées au commencement du tour du joueur
        public void commencerTour() {


        }

        // affichage du nom du joueur
        public void afficherNomJoueur(String line) {

            final String nomJoueur = line.split("-")[1];

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    tvNomJoueur = (TextView) findViewById(R.id.auTourDe);

                    tvNomJoueur.setText("Au tour de " + nomJoueur);

                }
            });
        }

        public void combattre(String nomJoueur, String levelJoueur, String attaqueJoueur, String nomMonstre, String levelMonstre, String attaqueMonstre) {

            PageMilieuCombatFragment f = new PageMilieuCombatFragment();

            // passage de paramètres au fragmentCombat
            Bundle args = new Bundle();
            args.putString("nomJoueur", nomJoueur);
            args.putString("levelJoueur", levelJoueur);
            args.putString("attaqueJoueur", attaqueJoueur);
            args.putString("nomMonstre", nomMonstre);
            args.putString("levelMonstre", levelMonstre);
            args.putString("attaqueMonstre", attaqueMonstre);
            f.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, f);
            transaction.commit();
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