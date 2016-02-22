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
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
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

    //paramètres listener
    public View.OnLongClickListener longClickListner;
    LinearLayout panel1, panel2, panel3, panel4, panel5, panel6;
    TextView text1, text2, text3, text4, text5;
    View openLayout;
    int nbPanels = 0;

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

        // pour garder les fragments en vie
        pager.setOffscreenPageLimit(3);

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

                    if (line.contains("afficherAccordeon")) {

                        afficherAccordeon(line);

                    }

                    if (line.contains("animationAccordeon")) {

                        animerAccordeon(nbPanels);

                    }

                    if (line.contains("afficherDansLeChat")) {

                        afficherDansLeChat(line);

                    }

                    if (line.contains("afficherMain")) {

                        afficherMain(line);

                    }

                }

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        public void afficherAccordeon(final String line) {

            nbPanels++;

            System.out.println("ca passe");

            // variables servant la conversion px -> dp
            final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
            final int padding = (int) (10 * scale + 0.5f);
            final int height = (int) (40 * scale + 0.5f);
            final int heightImage = (int) (120 * scale + 0.5f);
            final int widthImage = (int) (80 * scale + 0.5f);

            // variables
            final String nomJoueur = line.split("-")[1];
            String sexeJoueur = line.split("-")[2];
            int niveauJoueur = Integer.parseInt(line.split("-")[3]);
            int attaqueJoueur = Integer.parseInt(line.split("-")[4]);

            final int nombreClasses = Integer.parseInt(line.split("-")[5].substring(0, line.split("-")[5].indexOf("classe[")));
            System.out.println("nombreClasses : " + nombreClasses);

            int nombreRaces = Integer.parseInt(line.split("]-")[1].substring(0, line.split("]-")[1].indexOf("race[")));
            System.out.println("nombreRaces : " + nombreRaces);

            int nombreEquipements = Integer.parseInt(line.split("]-")[2].substring(0, line.split("]-")[2].indexOf("equipement[")));
            System.out.println("nombreEquipements : " + nombreEquipements);

            int nombreMaledictions;
            if (line.contains("malediction")) {
                nombreMaledictions = Integer.parseInt(line.split("]-")[3].substring(0, line.split("]-")[3].indexOf("malediction[")));
                System.out.println("nombreMaledictions : " + nombreMaledictions);
            }

            final String[] attributsClasse = line.substring(line.indexOf("classe[") + 7, line.indexOf("]")).split("-");
            String[] attributsRace = line.substring(line.indexOf("race[") + 5, line.indexOf("]", line.indexOf("race[") + 5)).split("-");
            String[] attributsEquipement = line.substring(line.indexOf("equipement[") + 11, line.indexOf("]", line.indexOf("equipement[") + 11)).split("-");
            String[] attributsMalediction = line.substring(line.indexOf("malediction[") + 12, line.indexOf("]", line.indexOf("malediction[") + 12)).split("-");

            // ne pas supprimer
            /*String nomRace = attributsRace[0];
            String descriptionRace = attributsRace[1];

            String nomEquipement = attributsEquipement[0];
            String descriptionEquipement = attributsEquipement[1];
            String partieCorpsEquipement = attributsEquipement[2];
            String grosEquipement = attributsEquipement[3];

            String nomMalediction = attributsMalediction[0];
            String descriptionMalediction = attributsClasse[1];*/

            // implémentation de l'accordéon
            runOnUiThread(new Runnable() {
                              @Override
                              public void run() {

                                  final LinearLayout layoutAccordeon = (LinearLayout) findViewById(R.id.layoutAccordeon);

                                  final LinearLayout layoutTest = new LinearLayout(getApplicationContext());
                                  layoutTest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                  layoutTest.setGravity(Gravity.CENTER);
                                  layoutTest.setOrientation(LinearLayout.VERTICAL);
                                  layoutTest.setVisibility(View.VISIBLE);
                                  layoutAccordeon.addView(layoutTest);

                                  // onglet
                                  final TextView tvTest = new TextView(getApplicationContext());
                                  LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, height);
                                  tvTest.setLayoutParams(lp);
                                  tvTest.setPadding(padding, padding, padding, padding);
                                  tvTest.setBackgroundColor(Color.parseColor("black"));
                                  tvTest.setTextColor(Color.parseColor("white"));
                                  tvTest.setText(nomJoueur);
                                  // id important
                                  tvTest.setId(nbPanels);
                                  tvTest.setVisibility(View.VISIBLE);
                                  layoutTest.addView(tvTest);

                                  // panel
                                  final LinearLayout layoutInterne = new LinearLayout(getApplicationContext());
                                  layoutInterne.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                  // id important
                                  layoutInterne.setId(100 + nbPanels);
                                  System.out.println("id : " + layoutInterne.getId());
                                  layoutInterne.setOrientation(LinearLayout.VERTICAL);
                                  layoutInterne.setVisibility(View.GONE);
                                  layoutTest.addView(layoutInterne);

                                  // image du joueur
                                  final ImageView imageJoueur = new ImageView(getApplicationContext());
                                  lp = new LinearLayout.LayoutParams(widthImage, heightImage);
                                  lp.setMargins(padding, padding, padding, padding);
                                  imageJoueur.setLayoutParams(lp);
                                  imageJoueur.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                  imageJoueur.setImageResource(R.drawable.image_test);
                                  layoutInterne.addView(imageJoueur);

                                  final TextView tvTitreRaceClasse = new TextView(getApplicationContext());
                                  lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                  lp.setMargins(padding, padding, padding, padding);
                                  tvTitreRaceClasse.setLayoutParams(lp);
                                  tvTitreRaceClasse.setText("Race(s) et Classe(s) : ");
                                  tvTitreRaceClasse.setTextColor(Color.parseColor("black"));
                                  layoutInterne.addView(tvTitreRaceClasse);

                                  final LinearLayout layoutMultiImages = new LinearLayout(getApplicationContext());
                                  layoutMultiImages.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                  layoutMultiImages.setOrientation(LinearLayout.HORIZONTAL);
                                  layoutInterne.addView(layoutMultiImages);

                                  for (int i = 0; i < nombreClasses; i = i + 2) {

                                      String nomClasse = attributsClasse[i];
                                      String descriptionClasse = attributsClasse[i + 1];

                                      final ImageView imageClasse = new ImageView(getApplicationContext());
                                      lp = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      lp.setMargins(padding, padding, padding, padding);
                                      imageClasse.setLayoutParams(lp);
                                      imageClasse.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                      imageClasse.setImageResource(getResources().getIdentifier(nomClasse, "drawable", getPackageName()));
                                      layoutMultiImages.addView(imageClasse);

                                  }
                              }
                          }
            );
        }

        public void afficherMain(final String line) {

            nbPanels++;

            System.out.println("ca passe envoyerMain");

            // variables servant la conversion px -> dp
            final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
            final int padding = (int) (10 * scale + 0.5f);
            final int height = (int) (40 * scale + 0.5f);
            final int heightImage = (int) (120 * scale + 0.5f);
            final int widthImage = (int) (80 * scale + 0.5f);

            // variables
            final String nomJoueur = line.split("-")[1];
            String sexeJoueur = line.split("-")[2];
            int niveauJoueur = Integer.parseInt(line.split("-")[3]);
            int attaqueJoueur = Integer.parseInt(line.split("-")[4]);

            int nombreClasses = 0;
            String[] attributsClasse = null;
            if (line.contains("classe")) {
                nombreClasses = Integer.parseInt(line.split("-")[5].substring(0, line.split("-")[5].indexOf("classe[")));
                attributsClasse = line.substring(line.indexOf("classe[") + 7, line.indexOf("]")).split("-");
                System.out.println("nombreClasses : " + nombreClasses);
            }

            int nombreRaces;
            String[] attributsRace;
            if (line.contains("race")) {
                nombreRaces = Integer.parseInt(line.split("]-")[1].substring(0, line.split("]-")[1].indexOf("race[")));
                attributsRace = line.substring(line.indexOf("race[") + 5, line.indexOf("]", line.indexOf("race[") + 5)).split("-");
                System.out.println("nombreRaces : " + nombreRaces);
            }

            int nombreEquipements;
            String[] attributsEquipement;
            if (line.contains("equipement")) {
                nombreEquipements = Integer.parseInt(line.split("]-")[2].substring(0, line.split("]-")[2].indexOf("equipement[")));
                attributsEquipement = line.substring(line.indexOf("equipement[") + 11, line.indexOf("]", line.indexOf("equipement[") + 11)).split("-");
                System.out.println("nombreEquipements : " + nombreEquipements);
            }

            int nombreMaledictions;
            String[] attributsMalediction;
            if (line.contains("malediction")) {
                nombreMaledictions = Integer.parseInt(line.split("]-")[3].substring(0, line.split("]-")[3].indexOf("malediction[")));
                attributsMalediction = line.substring(line.indexOf("malediction[") + 12, line.indexOf("]", line.indexOf("malediction[") + 12)).split("-");
                System.out.println("nombreMaledictions : " + nombreMaledictions);
            }

            int nombreMonstres;
            String[] attributsMonstres;
            if (line.contains("monstre")) {
                nombreMonstres = Integer.parseInt(line.split("]-")[4].substring(0, line.split("]-")[4].indexOf("monstre[")));
                attributsMonstres = line.substring(line.indexOf("monstre[") + 8, line.indexOf("]", line.indexOf("monstre[") + 8)).split("-");
                System.out.println("nombreMonstres : " + nombreMonstres);
            }

            int nombreBonus;
            String[] attributsBonus;
            if (line.contains("bonus")) {
                nombreBonus = Integer.parseInt(line.split("]-")[5].substring(0, line.split("]-")[5].indexOf("bonus[")));
                attributsBonus = line.substring(line.indexOf("bonus[") + 7, line.indexOf("]", line.indexOf("bonus[") + 7)).split("-");
                System.out.println("nombreBonus : " + nombreBonus);
            }

            // affichage de la main du joueur
            final int finalNombreClasses = nombreClasses;
            final String[] finalAttributsClasse = attributsClasse;
            runOnUiThread(new Runnable() {
                              @Override
                              public void run() {

                                  LinearLayout raceClasseLayout = (LinearLayout) findViewById(R.id.layoutRaceClasse);

                                  // affichage des races et classes en main
                                  for (int i = 0; i < finalNombreClasses; i = i + 2) {

                                      String nomClasse = finalAttributsClasse[i];
                                      String descriptionClasse = finalAttributsClasse[i + 1];

                                      final ImageView imageClasse = new ImageView(getApplicationContext());
                                      LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      lp.setMargins(padding, padding, padding, padding);
                                      imageClasse.setLayoutParams(lp);
                                      imageClasse.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                      imageClasse.setImageResource(getResources().getIdentifier(nomClasse, "drawable", getPackageName()));
                                      raceClasseLayout.addView(imageClasse);

                                  }

                              }
                          }
            );

            System.out.println("fin affichage main");
        }

        public void animerAccordeon(final int nbPanels) {

            for (int i = 1; i <= nbPanels; i++) {

                System.out.println("easy");
                int id = getApplicationContext().getResources().getIdentifier("" + i, "id", getApplicationContext().getPackageName());
                System.out.println("id : " + id);

                TextView textView = (TextView) findViewById(id);

                System.out.println("textview id : " + textView.getId());

                textView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        System.out.println("clic !");
                        hideOthers(v, nbPanels);
                    }
                });

            }
        }

        private void hideThemAll() {

            System.out.println("easy 3");

            if (openLayout == null) return;

            for (int i = 0; i < nbPanels; i++) {

                int idPanel = getApplicationContext().getResources().getIdentifier("" + 10 + i, "id", getApplicationContext().getPackageName());
                LinearLayout panel = (LinearLayout) findViewById(idPanel);

                if (openLayout == panel)
                    panel.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, panel, true));
            }
        }

        private void hideOthers(View layoutView, int nbPanels) {
            {
                int v;

                System.out.println("easy 2 ");

                for (int i = 1; i <= nbPanels; i++) {

                    int idPanel = getApplicationContext().getResources().getIdentifier("" + 10 + i, "id", getApplicationContext().getPackageName());
                    int idTextView = getApplicationContext().getResources().getIdentifier("" + i, "id", getApplicationContext().getPackageName());
                    LinearLayout panel = (LinearLayout) findViewById(idPanel);

                    if (layoutView.getId() == idTextView) {
                        v = panel.getVisibility();
                        hideThemAll();
                        if (v != View.VISIBLE) {
                            panel.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, panel, true));
                        }
                    }
                }
            }
        }

        public class ScaleAnimToHide extends ScaleAnimation {

            private View mView;

            private LinearLayout.LayoutParams mLayoutParams;

            private int mMarginBottomFromY, mMarginBottomToY;

            private boolean mVanishAfter = false;

            public ScaleAnimToHide(float fromX, float toX, float fromY, float toY, int duration, View view, boolean vanishAfter) {
                super(fromX, toX, fromY, toY);
                setDuration(duration);
                openLayout = null;
                mView = view;
                mVanishAfter = vanishAfter;
                mLayoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                int height = mView.getHeight();
                mMarginBottomFromY = (int) (height * fromY) + mLayoutParams.bottomMargin - height;
                mMarginBottomToY = (int) (0 - ((height * toY) + mLayoutParams.bottomMargin)) - height;

                Log.v("CZ", "height..." + height + " , mMarginBottomFromY...." + mMarginBottomFromY + " , mMarginBottomToY.." + mMarginBottomToY);
            }

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                if (interpolatedTime < 1.0f) {
                    int newMarginBottom = mMarginBottomFromY + (int) ((mMarginBottomToY - mMarginBottomFromY) * interpolatedTime);
                    mLayoutParams.setMargins(mLayoutParams.leftMargin, mLayoutParams.topMargin, mLayoutParams.rightMargin, newMarginBottom);
                    mView.getParent().requestLayout();
                    //Log.v("CZ","newMarginBottom..." + newMarginBottom + " , mLayoutParams.topMargin..." + mLayoutParams.topMargin);
                } else if (mVanishAfter) {
                    mView.setVisibility(View.GONE);
                }
            }
        }

        public class ScaleAnimToShow extends ScaleAnimation {

            private View mView;

            private LinearLayout.LayoutParams mLayoutParams;

            private int mMarginBottomFromY, mMarginBottomToY;

            private boolean mVanishAfter = false;

            public ScaleAnimToShow(float toX, float fromX, float toY, float fromY, int duration, View view, boolean vanishAfter) {
                super(fromX, toX, fromY, toY);
                openLayout = view;
                setDuration(duration);
                mView = view;
                mVanishAfter = vanishAfter;
                mLayoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                mView.setVisibility(View.VISIBLE);
                int height = mView.getHeight();
                //mMarginBottomFromY = (int) (height * fromY) + mLayoutParams.bottomMargin + height;
                //mMarginBottomToY = (int) (0 - ((height * toY) + mLayoutParams.bottomMargin)) + height;

                mMarginBottomFromY = 0;
                mMarginBottomToY = height;

                Log.v("CZ", ".................height..." + height + " , mMarginBottomFromY...." + mMarginBottomFromY + " , mMarginBottomToY.." + mMarginBottomToY);
            }

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                if (interpolatedTime < 1.0f) {
                    int newMarginBottom = (int) ((mMarginBottomToY - mMarginBottomFromY) * interpolatedTime) - mMarginBottomToY;
                    mLayoutParams.setMargins(mLayoutParams.leftMargin, mLayoutParams.topMargin, mLayoutParams.rightMargin, newMarginBottom);
                    mView.getParent().requestLayout();
                    //Log.v("CZ","newMarginBottom..." + newMarginBottom + " , mLayoutParams.topMargin..." + mLayoutParams.topMargin);
                }
            }

        }

        public void afficherDansLeChat(final String line) {

            final String messageAAfficher = line.substring(19);

            final String debutMessage = messageAAfficher.substring(0, messageAAfficher.indexOf(": "));
            final SpannableString spanString1 = new SpannableString(debutMessage);
            spanString1.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString1.length(), 0);
            spanString1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanString1.length(), 0);

            final String finMessage = messageAAfficher.substring(messageAAfficher.indexOf(": "));
            final SpannableString spanString2 = new SpannableString(finMessage);
            spanString2.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, spanString2.length(), 0);

            final LinearLayout chatView = (LinearLayout) findViewById(R.id.chatView);
            final EditText chatText = (EditText) findViewById(R.id.chatText);
            final ScrollView scrollViewChat = (ScrollView) findViewById(R.id.scrollViewChat);


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    final TextView tvChat = new TextView(getApplicationContext());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(10, 10, 10, 10);
                    tvChat.setLayoutParams(lp);
                    tvChat.append(spanString1);
                    tvChat.append(spanString2);
                    chatView.addView(tvChat);
                    // effacement du texte
                    chatText.setText("");
                    // on met le scroolview toujours en bas
                    scrollViewChat.fullScroll(View.FOCUS_DOWN);

                }
            });
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

    public void envoyerMessageChat(View v) {

        EditText ed = (EditText) findViewById(R.id.chatText);

        if (ed.getText() != null) {

            String texte = ed.getText().toString();

            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())),
                        true);
                out.println("clicBoutonChat" + texte);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}