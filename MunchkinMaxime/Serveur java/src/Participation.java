
/**
 * Classe qui représente l'aide dans le jeu , elle représente un joueur avec ce qu'il a demandé en échange de sa
 * participation au combat donc on aura autant de participations que de joueurs participants dans le combat - celui qui combat de base
 * 
 * Created by jojo on 03/02/2016.
 */
public class Participation {
    Joueur joueur_allie;
    int recompense_niveau_donne;
    int recompense_tresor_donne;

    public Participation(Joueur joueur_allie, int recompense_niveau_donne, int recompense_tresor_donne) {
        this.joueur_allie = joueur_allie;
        this.recompense_niveau_donne = recompense_niveau_donne;
        this.recompense_tresor_donne = recompense_tresor_donne;
    }

    public Joueur getJoueur_allie() {
        return joueur_allie;
    }

    public void setJoueur_allie(Joueur joueur_allie) {
        this.joueur_allie = joueur_allie;
    }

    public int getRecompense_niveau_donne() {
        return recompense_niveau_donne;
    }

    public void setRecompense_niveau_donne(int recompense_niveau_donne) {
        this.recompense_niveau_donne = recompense_niveau_donne;
    }

    public int getRecompense_tresor_donne() {
        return recompense_tresor_donne;
    }

    public void setRecompense_tresor_donne(int recompense_tresor_donne) {
        this.recompense_tresor_donne = recompense_tresor_donne;
    }
}
