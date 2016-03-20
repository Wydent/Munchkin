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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FragmentsMainActivity extends FragmentActivity {

    private PagerAdapter mPagerAdapter;
    private Socket socket;
    private static final int SERVERPORT = 6666;
    private static final String SERVER_IP = "10.0.2.2";
    InputStream stream = null;

    LinearLayout layoutPioche;
    ImageView ivPioche;
    TextView tvInfoBulle;
    TextView tvNomJoueur;
    TextView tvNomJoueurCombat = null;
    String nomJoueur = null;
    int idJoueur = 0;
    int nombreJoueurs = 0;

    Handler handlerTest;

    List fragments = null;

    //paramètres listener
    public View.OnLongClickListener longClickListner;
    LinearLayout panel1, panel2, panel3, panel4, panel5, panel6;
    TextView text1, text2, text3, text4, text5;
    View openLayout;

    private Menu menuContextuel = null;
    private Map<Integer, String> listItems = new HashMap<Integer, String>();
    private String selectionCarte = null;

    private String joueurCibleAide = null;
    private String tresorsDemandesAide = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.view_pager);

        // définition du nom du joueur
        Intent intent = getIntent();
        nomJoueur = intent.getStringExtra("PSEUDO");

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_carte_main, menu);

        menuContextuel = menu;

        // création du menu contextuel à partir de tous les items : id item + valeur item
        for (Map.Entry<Integer, String> e : listItems.entrySet()) {

            menuContextuel.add(0, e.getKey(), 0, e.getValue());

        }

        System.out.println("Menu contextuel attribué.");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        clicCarteMainBonusMalediction(selectionCarte, item.toString());

        return true;
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
                out.println(nomJoueur.toString());

                // mise sur écoute du client
                stream = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        stream));
                String line = "";
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Serveur dit : " + line);

                    if (line.contains("auTourDe")) {

                        afficherNomJoueur(line);

                    }

                    if (line.contains("afficherInfobulle")) {

                        String message = line.split("-")[1];

                        tvInfoBulle.setText(message);

                    }

                    if (line.contains("initAccordeon")) {

                        int idj = Integer.parseInt(line.substring(line.indexOf("Accordeon") + 9, line.indexOf("-")));

                        System.out.println("idj : " + idj);

                        initAccordeon(line, idj);

                    }

                    if (line.contains("afficherAccordeon")) {

                        int idj = Integer.parseInt(line.substring(line.indexOf("Accordeon") + 9, line.indexOf("-")));

                        System.out.println("idj : " + idj);

                        afficherAccordeon(line, idj);

                    }

                    if (line.contains("animationAccordeon")) {

                        animerAccordeon(nombreJoueurs);

                    }

                    if (line.contains("afficherDansLeChat")) {

                        afficherDansLeChat(line);

                    }

                    if (line.contains("afficherMain")) {

                        afficherMain(line);

                    }

                    if (line.contains("fuiteReussie")) {

                        fuir(line);

                    }

                    if (line.contains("fuiteEchouee")) {

                        fuir(line);

                    }

                    if (line.contains("nombreJoueurs")) {

                        nombreJoueurs = Integer.parseInt(line.split("-")[1]);

                    }

                    if (line.contains("idJoueur")) {

                        idJoueur = Integer.parseInt(line.split("-")[1]);

                    }

                    if (line.contains("lancerlinterfacecombat")) {

                        final String nomJoueur = line.split("-")[1];
                        final String levelJoueur = line.split("-")[2];
                        final String attaqueJoueur = line.split("-")[3];
                        final String nomImage = line.split("-")[4];
                        final String typeCarte = line.split("-")[5];
                        final String levelMonstre = line.split("-")[6];
                        final String ataqueMonstre = line.split("-")[7];
                        final String descriptionMonstre = line.split("-")[8];

                        combattre(line, nomJoueur, levelJoueur, attaqueJoueur, nomImage, levelMonstre, ataqueMonstre, descriptionMonstre);

                    }

                    if (line.contains("cartePiochee")) {

                        final String nomJoueur = line.split("-")[1];
                        final String levelJoueur = line.split("-")[2];
                        final String attaqueJoueur = line.split("-")[3];
                        final String nomImage = line.split("-")[4];
                        final String typeCarte = line.split("-")[5];
                        final String descriptionCarte = line.split("-")[6];
                        final String levelMonstre = line.split("-")[7];
                        final String recompensesTresors = line.split("-")[8];
                        final String recompensesNiveaux = line.split("-")[9];

                        afficherInfosCartePiochee(nomJoueur, nomImage);
                        afficherCartePiochee(nomImage, descriptionCarte, levelMonstre, recompensesTresors, recompensesNiveaux);

                    }

                    if (line.contains("actualiserMenu")) {

                        actualiserMenu(line);

                    }

                    if(line.contains("lancerlinterfacereponse")) {

                        final String joueurQuiDemande = line.split("-")[1];
                        final String joueurCible = line.split("-")[2];
                        final String tresors = line.split("-")[3];

                        repondre(line, joueurQuiDemande, joueurCible, tresors);

                    }

                }

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        public void fuir(final String line) {

            final Button fuirBouton = (Button) findViewById(R.id.boutonFuir);

            final int nb = Integer.parseInt(line.split("-")[1]);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fuirBouton.setClickable(false);
                    fuirBouton.setText(String.valueOf(nb));
                }
            });

            synchronized (this) {
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            PageMilieuFragment f = new PageMilieuFragment();

            // passage de paramètres au fragmentCombat
            Bundle args = new Bundle();
            args.putString("nomJoueur", nomJoueur);
            f.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, f);
            transaction.commit();

            final TextView infoBulle = (TextView) findViewById(R.id.infoBulle);

            if (nb > 4) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        infoBulle.setText("Fuite réussie !");
                    }
                });

            } else {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        infoBulle.setText("Fuite échouée !");
                    }
                });

            }

        }

        public void initAccordeon(final String line, final int idj) {

            // variables servant la conversion px -> dp
            final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
            final int padding = (int) (10 * scale + 0.5f);
            final int height = (int) (40 * scale + 0.5f);

            // variables
            final String nomJoueur = line.split("-")[1];

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
                    final TextView tvJoueur = new TextView(getApplicationContext());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, height);
                    tvJoueur.setLayoutParams(lp);
                    tvJoueur.setPadding(padding, padding, padding, padding);
                    tvJoueur.setBackgroundColor(Color.parseColor("black"));
                    tvJoueur.setTextColor(Color.parseColor("white"));
                    tvJoueur.setText(nomJoueur);
                    // id important
                    tvJoueur.setId(idj);
                    tvJoueur.setVisibility(View.VISIBLE);
                    layoutTest.addView(tvJoueur);

                    // panel
                    final LinearLayout layoutInterne = new LinearLayout(getApplicationContext());
                    layoutInterne.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    // id important
                    layoutInterne.setId(100 + idj);
                    layoutInterne.setOrientation(LinearLayout.VERTICAL);
                    layoutInterne.setVisibility(View.GONE);
                    layoutTest.addView(layoutInterne);

                    afficherAccordeon(line, idj);
                }
            });
        }

        public void afficherAccordeon(final String line, final int idj) {

            // variables servant la conversion px -> dp
            final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
            final int padding = (int) (10 * scale + 0.5f);
            final int height = (int) (40 * scale + 0.5f);
            final int heightImage = (int) (120 * scale + 0.5f);
            final int widthImage = (int) (80 * scale + 0.5f);

            // variables
            final String nomJoueur = line.split("-")[1];
            String sexeJoueur = line.split("-")[2];
            final int niveauJoueur = Integer.parseInt(line.split("-")[3]);
            final int attaqueJoueur = Integer.parseInt(line.split("-")[4]);

            final int nombreClasses = Integer.parseInt(line.split("-")[5].substring(0, line.split("-")[5].indexOf("classe[")));

            final int nombreRaces = Integer.parseInt(line.split("]-")[1].substring(0, line.split("]-")[1].indexOf("race[")));

            final int nombreEquipements = Integer.parseInt(line.split("]-")[2].substring(0, line.split("]-")[2].indexOf("equipement[")));

            final int nombreMaledictions = Integer.parseInt(line.split("]-")[3].substring(0, line.split("]-")[3].indexOf("malediction[")));

            String[] attributsClasse = null;
            String[] attributsRace = null;
            String[] attributsEquipement = null;
            String[] attributsMalediction = null;

            if (nombreClasses != 0)
                attributsClasse = line.substring(line.indexOf("classe[") + 7, line.indexOf("]")).split("-");

            attributsRace = line.substring(line.indexOf("race[") + 5, line.indexOf("]", line.indexOf("race[") + 5)).split("-");

            if (nombreEquipements != 0)
                attributsEquipement = line.substring(line.indexOf("equipement[") + 11, line.indexOf("]", line.indexOf("equipement[") + 11)).split("-");

            if (nombreMaledictions != 0)
                attributsMalediction = line.substring(line.indexOf("malediction[") + 12, line.indexOf("]", line.indexOf("malediction[") + 12)).split("-");

            // implémentation de l'accordéon
            final String[] finalAttributsClasse = attributsClasse;
            final String[] finalAttributsRace = attributsRace;
            final String[] finalAttributsEquipement = attributsEquipement;
            final String[] finalAttributsMalediction = attributsMalediction;
            runOnUiThread(new Runnable() {
                              @Override
                              public void run() {

                                  int id = getResources().getIdentifier(100 + idj + "", "id", getPackageName());
                                  final LinearLayout layoutInterne = (LinearLayout) findViewById(id);

                                  // on réinitialise l'affichage
                                  layoutInterne.removeAllViews();

                                  LinearLayout.LayoutParams lp = null;

                                  // level du joueur
                                  final TextView tvLevel = new TextView(getApplicationContext());
                                  tvLevel.setText("lvl " + niveauJoueur);
                                  tvLevel.setTextColor(Color.parseColor("green"));
                                  layoutInterne.addView(tvLevel);

                                  // attaque du joueur
                                  final TextView tvAttaque = new TextView(getApplicationContext());
                                  tvLevel.setText("force " + attaqueJoueur);
                                  tvLevel.setTextColor(Color.parseColor("blue"));
                                  layoutInterne.addView(tvAttaque);

                                  // image du joueur
                                  final ImageView imageJoueur = new ImageView(getApplicationContext());
                                  lp = new LinearLayout.LayoutParams(widthImage, heightImage);
                                  lp.setMargins(padding, padding, padding, padding);
                                  imageJoueur.setLayoutParams(lp);
                                  imageJoueur.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                  imageJoueur.setImageResource(R.drawable.image_test);
                                  layoutInterne.addView(imageJoueur);

                                  /* races et classes */
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
                                  layoutMultiImages.setMinimumHeight(heightImage);
                                  layoutMultiImages.setBackgroundColor(Color.parseColor("red"));
                                  layoutInterne.addView(layoutMultiImages);

                                  for (int i = 0; i < nombreClasses * 2; i = i + 2) {

                                      String nomClasse = finalAttributsClasse[i];
                                      String descriptionClasse = finalAttributsClasse[i + 1];

                                      final ImageView imageClasse = new ImageView(getApplicationContext());
                                      lp = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      lp.setMargins(padding, padding, padding, padding);
                                      imageClasse.setLayoutParams(lp);
                                      imageClasse.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                      imageClasse.setImageResource(getResources().getIdentifier(new String(nomClasse).replaceAll("[éè]", "e").replaceAll(" ", "_").toLowerCase(), "drawable", getPackageName()));
                                      layoutMultiImages.addView(imageClasse);

                                  }

                                  for (int i = 0; i < nombreRaces * 2; i = i + 2) {

                                      String nomRace = finalAttributsRace[i];
                                      String descriptionRace = finalAttributsRace[i + 1];

                                      final ImageView imageRace = new ImageView(getApplicationContext());
                                      lp = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      lp.setMargins(padding, padding, padding, padding);
                                      imageRace.setLayoutParams(lp);
                                      imageRace.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                      imageRace.setImageResource(getResources().getIdentifier(new String(nomRace).replaceAll("[éè]", "e").replaceAll(" ", "_").toLowerCase(), "drawable", getPackageName()));
                                      layoutMultiImages.addView(imageRace);

                                  }

                                  /* equipements */
                                  final TextView tvTitreEquipement = new TextView(getApplicationContext());
                                  lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                  lp.setMargins(padding, padding, padding, padding);
                                  tvTitreEquipement.setLayoutParams(lp);
                                  tvTitreEquipement.setText("Equipements : ");
                                  tvTitreEquipement.setTextColor(Color.parseColor("black"));
                                  layoutInterne.addView(tvTitreEquipement);

                                  final LinearLayout layoutMultiImages2 = new LinearLayout(getApplicationContext());
                                  layoutMultiImages2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                  layoutMultiImages2.setOrientation(LinearLayout.HORIZONTAL);
                                  layoutMultiImages2.setMinimumHeight(heightImage);
                                  layoutMultiImages2.setBackgroundColor(Color.parseColor("purple"));
                                  layoutInterne.addView(layoutMultiImages2);

                                  for (int i = 0; i < nombreEquipements * 4; i = i + 4) {

                                      String nomEquipement = finalAttributsEquipement[i];
                                      String descriptionEquipement = finalAttributsEquipement[i + 1];
                                      String partieCorps = finalAttributsEquipement[i + 2];
                                      String isGros = finalAttributsEquipement[i + 3];

                                      final ImageView imageEquipement = new ImageView(getApplicationContext());
                                      lp = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      lp.setMargins(padding, padding, padding, padding);
                                      imageEquipement.setLayoutParams(lp);
                                      imageEquipement.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                      imageEquipement.setImageResource(getResources().getIdentifier(new String(nomEquipement).replaceAll("[éè]", "e").replaceAll(" ", "_").toLowerCase(), "drawable", getPackageName()));
                                      layoutMultiImages2.addView(imageEquipement);

                                  }

                                  /* malédictions et bonus */
                                  final TextView tvTitreMaledictionBonus = new TextView(getApplicationContext());
                                  lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                  lp.setMargins(padding, padding, padding, padding);
                                  tvTitreMaledictionBonus.setLayoutParams(lp);
                                  tvTitreMaledictionBonus.setText("Malédictions et bonus : ");
                                  tvTitreMaledictionBonus.setTextColor(Color.parseColor("black"));
                                  layoutInterne.addView(tvTitreMaledictionBonus);

                                  final LinearLayout layoutMultiImages3 = new LinearLayout(getApplicationContext());
                                  layoutMultiImages3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                  layoutMultiImages3.setOrientation(LinearLayout.HORIZONTAL);
                                  layoutMultiImages3.setMinimumHeight(heightImage);
                                  layoutMultiImages3.setBackgroundColor(Color.parseColor("green"));
                                  layoutInterne.addView(layoutMultiImages3);

                                  for (int i = 0; i < nombreMaledictions * 2; i = i + 2) {

                                      String nom = finalAttributsMalediction[i];
                                      String description = finalAttributsMalediction[i + 1];

                                      final ImageView image = new ImageView(getApplicationContext());
                                      lp = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      lp.setMargins(padding, padding, padding, padding);
                                      image.setLayoutParams(lp);
                                      image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                      image.setImageResource(getResources().getIdentifier(new String(nom).replaceAll("[éè]", "e").replaceAll(" ", "_").toLowerCase(), "drawable", getPackageName()));
                                      layoutMultiImages3.addView(image);

                                  }

                              }
                          }
            );

        }

        public void afficherMain(final String line) {

            final LinearLayout monstreLayout = (LinearLayout) findViewById(R.id.layoutMonstre);
            final LinearLayout bonusMaledictionLayout = (LinearLayout) findViewById(R.id.layoutMaledictionBonus);
            final LinearLayout equipementLayout = (LinearLayout) findViewById(R.id.layoutEquipement);
            final LinearLayout raceClasseLayout = (LinearLayout) findViewById(R.id.layoutRaceClasse);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // on réinitialise la vue
                    monstreLayout.removeAllViews();
                    bonusMaledictionLayout.removeAllViews();
                    equipementLayout.removeAllViews();
                    raceClasseLayout.removeAllViews();
                }
            });

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

            int nombreClasses = Integer.parseInt(line.split("-")[5].substring(0, line.split("-")[5].indexOf("classe[")));
            String[] attributsClasse = null;
            if (nombreClasses != 0) {
                attributsClasse = line.substring(line.indexOf("classe[") + 7, line.indexOf("]")).split("-");
            }

            int nombreRaces = Integer.parseInt(line.split("]-")[1].substring(0, line.split("]-")[1].indexOf("race[")));
            String[] attributsRace = new String[0];
            if (nombreRaces != 0) {
                attributsRace = line.substring(line.indexOf("race[") + 5, line.indexOf("]", line.indexOf("race[") + 5)).split("-");
            }

            int nombreEquipements = Integer.parseInt(line.split("]-")[2].substring(0, line.split("]-")[2].indexOf("equipement[")));
            String[] attributsEquipement = new String[0];
            if (nombreEquipements != 0) {
                attributsEquipement = line.substring(line.indexOf("equipement[") + 11, line.indexOf("]", line.indexOf("equipement[") + 11)).split("-");
            }

            int nombreMaledictions = Integer.parseInt(line.split("]-")[3].substring(0, line.split("]-")[3].indexOf("malediction[")));
            String[] attributsMalediction = new String[0];
            if (nombreMaledictions != 0) {
                attributsMalediction = line.substring(line.indexOf("malediction[") + 12, line.indexOf("]", line.indexOf("malediction[") + 12)).split("-");
            }

            int nombreMonstres = Integer.parseInt(line.split("]-")[4].substring(0, line.split("]-")[4].indexOf("monstre[")));
            String[] attributsMonstres = new String[0];
            if (nombreMonstres != 0) {
                attributsMonstres = line.substring(line.indexOf("monstre[") + 8, line.indexOf("]", line.indexOf("monstre[") + 8)).split("-");
            }

            int nombreBonus = Integer.parseInt(line.split("]-")[5].substring(0, line.split("]-")[5].indexOf("bonus[")));
            String[] attributsBonus = new String[0];
            if (nombreBonus != 0) {
                attributsBonus = line.substring(line.indexOf("bonus[") + 6, line.indexOf("]", line.indexOf("bonus[") + 6)).split("-");
            }

            // affichage de la main du joueur
            final int finalNombreClasses = nombreClasses;
            final int finalNombreRaces = nombreRaces;
            final String[] finalAttributsClasse = attributsClasse;
            final String[] finalAttributsRace = attributsRace;
            final String[] finalAttributsEquipement = attributsEquipement;
            final int finalNombreMonstres = nombreMonstres;
            final String[] finalAttributsMonstres = attributsMonstres;
            final String[] finalAttributsMalediction = attributsMalediction;
            final int finalNombreMaledictions = nombreMaledictions;
            final int finalNombreBonus = nombreBonus;
            final String[] finalAttributsBonus = attributsBonus;
            final int finalNombreEquipements = nombreEquipements;
            runOnUiThread(new Runnable() {
                              @Override
                              public void run() {

                                  // affichage des races et classes en main
                                  for (int i = 0; i < finalNombreClasses * 2; i = i + 2) {

                                      String nomClasse = finalAttributsClasse[i];
                                      String descriptionClasse = finalAttributsClasse[i + 1];

                                      final ImageView imageClasse = new ImageView(getApplicationContext());
                                      LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      lp.setMargins(padding, padding, padding, padding);
                                      imageClasse.setLayoutParams(lp);
                                      imageClasse.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                      imageClasse.setImageResource(getResources().getIdentifier(new String(nomClasse).replaceAll("[éè]", "e").replaceAll(" ", "_").toLowerCase(), "drawable", getPackageName()));
                                      raceClasseLayout.addView(imageClasse);

                                  }

                                  for (int i = 0; i < finalNombreRaces * 2; i = i + 2) {

                                      String nomRace = finalAttributsRace[i];
                                      String descriptionRace = finalAttributsRace[i + 1];

                                      final ImageView imageRace = new ImageView(getApplicationContext());
                                      LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      lp.setMargins(padding, padding, padding, padding);
                                      imageRace.setLayoutParams(lp);
                                      imageRace.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                      imageRace.setImageResource(getResources().getIdentifier(new String(nomRace).replaceAll("[éè]", "e").replaceAll(" ", "_").toLowerCase(), "drawable", getPackageName()));
                                      raceClasseLayout.addView(imageRace);

                                  }

                                  // affichage des équipements en main
                                  for (int i = 0; i < finalNombreEquipements * 4; i = i + 4) {

                                      final String nomEquipement = finalAttributsEquipement[i];
                                      String descriptionEquipement = finalAttributsEquipement[i + 1];
                                      String partieCorps = finalAttributsEquipement[i + 2];
                                      String isGros = finalAttributsEquipement[i + 3];

                                      final RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());
                                      LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      relativeLayout.setLayoutParams(lp);

                                      final ImageView imageEquipement = new ImageView(getApplicationContext());
                                      LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      lp.setMargins(padding, padding, padding, padding);
                                      imageEquipement.setLayoutParams(lp2);
                                      imageEquipement.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                      imageEquipement.setImageResource(getResources().getIdentifier(new String(nomEquipement).replaceAll("[éè]", "e").replaceAll(" ", "_").toLowerCase(), "drawable", getPackageName()));
                                      imageEquipement.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                              clicCarteMainEquipement(nomEquipement);

                                          }
                                      });

                                      TextView tvDescriptionCarte = new TextView(getApplicationContext());
                                      tvDescriptionCarte.setTextColor(Color.parseColor("black"));
                                      LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      tvDescriptionCarte.setLayoutParams(lp4);
                                      tvDescriptionCarte.setText(descriptionEquipement);

                                      LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                                      LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      linearLayout.setLayoutParams(lp3);
                                      linearLayout.addView(tvDescriptionCarte);


                                      equipementLayout.addView(relativeLayout);
                                      relativeLayout.addView(imageEquipement);
                                      relativeLayout.addView(linearLayout);

                                  }

                                  // affichage des monstres en main
                                  for (int i = 0; i < finalNombreMonstres * 5; i = i + 5) {

                                      final String nomMonstre = finalAttributsMonstres[i];
                                      String descriptionMonstre = finalAttributsMonstres[i + 1];
                                      String niveauMonstre = finalAttributsMonstres[i + 2];
                                      String recompenseNiveaux = finalAttributsMonstres[i + 3];
                                      String recompenseTresors = finalAttributsMonstres[i + 4];

                                      final ImageView imageMonstre = new ImageView(getApplicationContext());
                                      LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      lp.setMargins(padding, padding, padding, padding);
                                      imageMonstre.setLayoutParams(lp);
                                      imageMonstre.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                      imageMonstre.setImageResource(getResources().getIdentifier(new String(nomMonstre).replaceAll("[éè]", "e").replaceAll(" ", "_").toLowerCase(), "drawable", getPackageName()));
                                      imageMonstre.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                              clicCarteMainEquipement(nomMonstre);

                                          }
                                      });
                                      monstreLayout.addView(imageMonstre);

                                  }

                                  // affichage des bonus en main
                                  for (int i = 0; i < finalNombreBonus * 2; i = i + 2) {

                                      final String nomBonus = finalAttributsBonus[i];
                                      String descriptionBonus = finalAttributsBonus[i + 1];

                                      final RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());
                                      LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      relativeLayout.setLayoutParams(lp);

                                      final ImageView imageBonus = new ImageView(getApplicationContext());
                                      LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      lp.setMargins(padding, padding, padding, padding);
                                      imageBonus.setLayoutParams(lp2);
                                      imageBonus.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                      imageBonus.setImageResource(getResources().getIdentifier(new String(nomBonus).replaceAll("[éè]", "e").replaceAll(" ", "_").toLowerCase(), "drawable", getPackageName()));
                                      registerForContextMenu(imageBonus);
                                      imageBonus.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                              selectionCarte = nomBonus;

                                              v.showContextMenu();

                                          }
                                      });

                                      TextView tvDescriptionCarte = new TextView(getApplicationContext());
                                      tvDescriptionCarte.setTextColor(Color.parseColor("black"));
                                      LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      tvDescriptionCarte.setLayoutParams(lp4);
                                      tvDescriptionCarte.setText(descriptionBonus);

                                      LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                                      LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      linearLayout.setLayoutParams(lp3);
                                      linearLayout.addView(tvDescriptionCarte);

                                      bonusMaledictionLayout.addView(relativeLayout);
                                      relativeLayout.addView(imageBonus);
                                      relativeLayout.addView(linearLayout);

                                  }

                                  // affichage des malédictions en main
                                  for (int i = 0; i < finalNombreMaledictions * 2; i = i + 2) {

                                      final String nomMalediction = finalAttributsMalediction[i];
                                      String descriptionMalediction = finalAttributsMalediction[i + 1];

                                      final RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());
                                      LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      relativeLayout.setLayoutParams(lp);

                                      final ImageView imageMalediction = new ImageView(getApplicationContext());
                                      LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      lp.setMargins(padding, padding, padding, padding);
                                      imageMalediction.setLayoutParams(lp2);
                                      imageMalediction.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                      imageMalediction.setImageResource(getResources().getIdentifier(new String(nomMalediction).replaceAll("[éè]", "e").replaceAll(" ", "_").toLowerCase(), "drawable", getPackageName()));
                                      registerForContextMenu(imageMalediction);
                                      imageMalediction.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                              selectionCarte = nomMalediction;

                                              v.showContextMenu();

                                          }
                                      });

                                      TextView tvDescriptionCarte = new TextView(getApplicationContext());
                                      tvDescriptionCarte.setTextColor(Color.parseColor("black"));
                                      LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      tvDescriptionCarte.setLayoutParams(lp4);
                                      tvDescriptionCarte.setText(descriptionMalediction);

                                      LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                                      LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(widthImage, heightImage);
                                      linearLayout.setLayoutParams(lp3);
                                      linearLayout.addView(tvDescriptionCarte);

                                      bonusMaledictionLayout.addView(relativeLayout);
                                      relativeLayout.addView(imageMalediction);
                                      relativeLayout.addView(linearLayout);

                                  }
                              }
                          }
            );

            System.out.println("fin affichage main");
        }

        public void animerAccordeon(final int nbPanels) {

            for (int i = 1; i <= nbPanels; i++) {

                int id = getApplicationContext().getResources().getIdentifier("" + i, "id", getApplicationContext().getPackageName());

                System.out.println("id : " + id);

                TextView textView = (TextView) findViewById(id);

                textView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        hideOthers(v, nbPanels);
                    }
                });

            }
        }

        private void hideThemAll() {

            if (openLayout == null) return;

            for (int i = 0; i < nombreJoueurs; i++) {

                int idPanel = getApplicationContext().getResources().getIdentifier("" + 10 + i, "id", getApplicationContext().getPackageName());
                LinearLayout panel = (LinearLayout) findViewById(idPanel);

                if (openLayout == panel)
                    panel.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, panel, true));
            }
        }

        private void hideOthers(View layoutView, int nbPanels) {
            {
                int v;

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

        // affichage des infos dans l'infobulle
        public void afficherInfosCartePiochee(final String nomJoueur, final String nomImage) {

            System.out.println("nomJoueur : " + nomJoueur);
            System.out.println("nomImage : " + nomImage);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvInfoBulle = (TextView) findViewById(R.id.infoBulle);
                    // on rend visible l'infobulle
                    tvInfoBulle.setVisibility(View.VISIBLE);
                    tvInfoBulle.setText(nomJoueur + " a pioché la carte " + nomImage);
                }
            });
        }

        // affichage de la carte piochée

        public void afficherCartePiochee(final String nomImage, final String descriptionCarte, final String niveauCarte, final String recompensesTresors, final String recompensesNiveaux) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ivPioche = (ImageView) findViewById(R.id.imagePioche);
                    layoutPioche = (LinearLayout) findViewById(R.id.layoutPioche);

                    // réinitialisation de l'affichage
                    layoutPioche.removeAllViews();

                    // affichage de la carte
                    ivPioche.setImageResource(getResources().getIdentifier(new String(nomImage).replaceAll("[éè]", "e").replaceAll(" ", "_").toLowerCase(), "drawable", getPackageName()));

                    // affichage du niveau du monstre
                    TextView tv = new TextView(getApplicationContext());
                    tv.setText("level " + niveauCarte);
                    tv.setTextColor(Color.parseColor("green"));
                    layoutPioche.addView(tv);

                    // affichage des récompenses tresors
                    TextView tv2 = new TextView(getApplicationContext());
                    tv2.setText("tresors : " + recompensesTresors);
                    tv2.setTextColor(Color.parseColor("purple"));
                    layoutPioche.addView(tv2);

                    // affichage des récompenses niveaux
                    TextView tv4 = new TextView(getApplicationContext());
                    tv4.setText("niveaux offerts : " + recompensesTresors);
                    tv4.setTextColor(Color.parseColor("red"));
                    layoutPioche.addView(tv4);

                    // affichage de la description de la carte
                    TextView tv3 = new TextView(getApplicationContext());
                    tv3.setText(descriptionCarte);
                    tv3.setTextColor(Color.parseColor("black"));
                    layoutPioche.addView(tv3);
                }
            });
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

        public void repondre(String line, String joueurQuiDemande, String joueurCible, String tresors) {

            ReponseAideFragment f = new ReponseAideFragment();

            // passage de paramètres au fragmentCombat
            Bundle args = new Bundle();
            args.putString("joueurQuiDemande", joueurQuiDemande);
            args.putString("joueurCible", joueurCible);
            args.putString("tresors", tresors);

            f.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, f);
            transaction.commit();

        }

        public void combattre(String line, String nomJoueur, String levelJoueur, String attaqueJoueur, String nomMonstre, String levelMonstre, String attaqueMonstre, String descriptionMonstre) {

            String joueurActuel = line.split("-")[1];

            PageMilieuCombatFragment f = new PageMilieuCombatFragment();

            // passage de paramètres au fragmentCombat
            Bundle args = new Bundle();
            args.putString("nomJoueur", nomJoueur);
            args.putString("levelJoueur", levelJoueur);
            args.putString("attaqueJoueur", attaqueJoueur);
            args.putString("nomMonstre", nomMonstre);
            args.putString("levelMonstre", levelMonstre);
            args.putString("attaqueMonstre", attaqueMonstre);
            args.putString("descriptionMonstre", descriptionMonstre);
            // si c'est à nous de jouer
            if (joueurActuel.equals(nomJoueur)) {

                args.putBoolean("isTonTour", true);

            } else {

                args.putBoolean("isTonTour", false);

            }

            f.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, f);
            transaction.commit();
        }
    }

    /******************************************
     * FIN THREAD
     **************************************/

    public void finDeTour(View v) {

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println("changementTour");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

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

    public void test2(View v) {

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println("clicPiocheTresor");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void clicCarteMainEquipement(String nomEquipement) {

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println("clicCarteMainEquipement-" + nomEquipement);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void clicCarteMainBonusMalediction(String nomBonusMalediction, String joueurCible) {

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println("clicCarteMainBonusMalediction-" + nomBonusMalediction + "-" + joueurCible);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void clicBoutonFuir(View v) {

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println("clicBoutonFuir");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clicBoutonCombattre(View v) {

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println("clicBoutonCombattre");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PageMilieuFragment f = new PageMilieuFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, f);
        transaction.commit();

    }

    public void clicBoutonAide(View v) {

        AideFragment f = new AideFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, f);
        transaction.commit();

    }

    public void validerAide(View v) {

        EditText joueurCible = (EditText) findViewById(R.id.etJoueurCible);
        EditText tresorsDemandes = (EditText) findViewById(R.id.etTresorsDemandes);

        joueurCibleAide = joueurCible.getText().toString();
        tresorsDemandesAide = tresorsDemandes.getText().toString();

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println("demanderAide-" + joueurCible.getText() + "-" + tresorsDemandes.getText());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void reponseAideOui(View v) {

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println("validerAide-" + joueurCibleAide + "-" + tresorsDemandesAide);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void reponseAideNon(View v) {

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println("refuserAide");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void actualiserMenu(final String line) {

        String[] listeJoueurs = line.substring(line.indexOf("actualiserMenu-") + 15).split("-");

        System.out.println("liste joueurs : " + listeJoueurs);

        for (int i = 0; i < listeJoueurs.length; i++) {

            int id = i;
            listItems.put(id, listeJoueurs[i]);

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