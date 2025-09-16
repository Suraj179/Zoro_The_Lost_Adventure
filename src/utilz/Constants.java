package utilz;
import main.Game;

public class Constants {

    // public static class EnemyConstants{
    //     public static final int CRABBY = 0;

    //     public static final int IDLE = 0;
    //     public static final int RUNNING = 1;
    //     public static final int ATTACK = 2;
    //     public static final int HIT = 3;
    //     public static final int DEAD = 4;

    //     public static final int CRABBY_WIDTH_DEFAULT = 72;
    //     public static final int CRABBY_HEIGHT_DEFAULT = 32;

    //     public static final int CRABBY_WIDTH = (int)(CRABBY_WIDTH_DEFAULT * Game.SCALE);
    //     public static final int CRABBY_HEIGHT = (int)(CRABBY_HEIGHT_DEFAULT * Game.SCALE);

    //     public static final int CRABBY_DRAWOFFSET_X =(int)(26 * Game.SCALE);
    //     public static final int CRABBY_DRAWOFFSET_Y =(int)(9 * Game.SCALE);
        

    //     public static int GetSpriteAmount(int enemyType, int enemyState){
    //         switch(enemyType){
    //             case CRABBY:
    //                 switch(enemyState){
    //                     case IDLE: return 9;
    //                     case RUNNING: return 6;
    //                     case ATTACK: return 7;
    //                     case HIT: return 4;
    //                     case DEAD: return 5;
    //                 }
    //         }
    //         return 0;
    //     }
    // }

     public static class EnemyConstants{
        public static final int PIRATE = 0;

        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static final float PIRATE_SCALE=0.65f* Game.SCALE;

        public static final int PIRATE_WIDTH_DEFAULT = 63;//40
        public static final int PIRATE_HEIGHT_DEFAULT = 63;//42

        public static final int PIRATE_WIDTH = (int)(PIRATE_WIDTH_DEFAULT * PIRATE_SCALE);
        public static final int PIRATE_HEIGHT = (int)(PIRATE_HEIGHT_DEFAULT * PIRATE_SCALE);

        public static final int PIRATE_DRAWOFFSET_X =(int)(27 * PIRATE_SCALE);
        public static final int PIRATE_DRAWOFFSET_Y =(int)(19 * PIRATE_SCALE);

        public static final int PIRATE_HITBOX_WIDTH_DEFAULT=37;
        public static final int PIRATE_HITBOX_HEIGHT_DEFAULT=42;

        public static final int PIRATE_HITBOX_WIDTH=(int)(PIRATE_HITBOX_WIDTH_DEFAULT * PIRATE_SCALE);
        public static final int PIRATE_HITBOX_HEIGHT=(int)(PIRATE_HITBOX_HEIGHT_DEFAULT * PIRATE_SCALE);


        public static int GetSpriteAmount(int enemyType, int enemyState){
            switch(enemyType){
                case PIRATE:
                    switch(enemyState){
                        case IDLE: return 3;
                        case RUNNING: return 6;
                        case ATTACK: return 5;
                        case HIT: return 5;
                        case DEAD: return 5;
                    }
            }
            return 0;
        }

        public static int GetMaxHealth(int enemyType){
            switch(enemyType){
                case PIRATE:
                    return 30;
                default:
                    return 1;
            }
        }

        public static int GetEnemyDmg(int enemy_type){
            switch(enemy_type){
                case PIRATE:
                    return 15;
                default :
                    return 0;
            }
        }
    }

    public static class Environment{
        public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
        public static final int BIG_CLOUDS_HEIGHT_DEFAULT = 101;

        public static final int BIG_CLOUD_WIDTH = (int)(BIG_CLOUD_WIDTH_DEFAULT * Game.SCALE);
        public static final int BIG_CLOUD_HEIGHT = (int)(BIG_CLOUDS_HEIGHT_DEFAULT * Game.SCALE);

        public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;
        public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;

        public static final int SMALL_CLOUD_HEIGHT = (int)(SMALL_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
        public static final int SMALL_CLOUD_WIDTH = (int)(SMALL_CLOUD_WIDTH_DEFAULT * Game.SCALE);

    }

    public static class UI{
        public static class Buttons{
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int)(B_WIDTH_DEFAULT * Game.SCALE);
             public static final int B_HEIGHT = (int)(B_HEIGHT_DEFAULT * Game.SCALE);
        }

        public static class PauseButtons{
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int)(SOUND_SIZE_DEFAULT * Game.SCALE);
            
        }

        public static class URMButtons {
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Game.SCALE);
        }

        public static class VolumeButtons{
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH = (int)(VOLUME_DEFAULT_WIDTH * Game.SCALE);
            public static final int VOLUME_HEIGHT = (int)(VOLUME_DEFAULT_HEIGHT * Game.SCALE);
            public static final int SLIDER_WIDTH = (int)(SLIDER_DEFAULT_WIDTH * Game.SCALE);

        }
    }

    public static class Directions{
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }
    public static class PlayerConstants{
        public static final int IDLE = 0;
        public static final int  RUNNING = 1;
        public static final int RUN_FAST = 2;
        public static final int ATTACKJUMP1 = 3;
        public static final int ATTACK1 = 4;
        public static final int ATTACK2 = 5;
        public static final int ATTACK3 = 6;
        public static final int DAMAGE1 = 7;
        public static final int WAKE_UP = 8;
        public static final int QUICK_NAP = 9;
        public static final int JUMP = 10;
        public static final int SHEATHING = 11;
        public static final int HIT = 12;
        public static final int ATTACK4 = 13;
        public static final int BLOCK = 14;

        public static String playerActionString(int action){
            if(action==0)return "IDLE";
            else if(action==1)return "RUNNING";
            else if(action==2)return "RUN_FAST";
            else if(action==3)return "ATTACKJUMP1";
            else if(action==4)return "ATTACK1";
            else if(action==5)return "ATTACK2";//punch
            else if(action==6)return "ATTACK3";//onigiri
            else if(action==7)return "DAMAGE1";//sneezing
            else if(action==8)return "WAKE_UP";//wakeup after falling
            else if(action==9)return "QUICK_NAP";
            else if(action==10)return "JUMP"; //Jumping with sword
            else if(action==11)return "SHEATHING";
            else if(action==12)return "HIT"; //get hit and fall
            else if(action==13)return "ATTACK4";//two sword style
            else if(action==14)return "BLOCK";//BLOCK THE ATTACK
            else{
                return "nill";
            }
        }
        public static int GetSpriteAmount(int player_action){
            switch(player_action){//counting of image starts from 1 not from 0 like of index
                
                case IDLE: return 4;
                case RUNNING: return 8;
                case RUN_FAST: return 2;
                case ATTACKJUMP1: return 9;
                case ATTACK1: return 9;
                case ATTACK2: return 9;
                case ATTACK3: return 15;
                case DAMAGE1: return 4;
                case WAKE_UP: return 6;
                case QUICK_NAP: return 7;
                case JUMP: return 6;
                case SHEATHING: return 6;
                case HIT: return 5;
                case ATTACK4: return 6;
                case BLOCK: return 3;
                default:
                    return 1;
            }
        }
    }
}
