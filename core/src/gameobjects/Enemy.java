package gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import Tweens.Value;
import Tweens.ValueAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import configuration.Configuration;
import gameworld.GameWorld;
import helpers.AssetLoader;

/**
 * Created by ManuGil on 05/02/15.
 */
public class Enemy {


    public enum EnemyState {
        WAITING, MOVING, DEAD, CENTER, FINISH, MOVING_TO_CENTER
    }

    private GameWorld world;
    private Sprite sprite, backSprite, bonusprite;
    public Vector2 position, velocity, acceleration, point;
    private int height, width;
    private int maxVelocity;
    //VARIABLES
    private Rectangle rectangle;
    //TWEEN
    private TweenManager manager;

    private int type;
    private EnemyState enemyState;
    private boolean velSetted = false;

    private Value centerValue = new Value();
    private Value scaleValue = new Value();

    private int angle, angleInc;
    private float alpha;

    public boolean bonus = false;

    public Enemy(GameWorld world, float x, float y, float width, float height) {

        //GENERAL VARIABLES
        this.world = world;

        //Position
        this.position = new Vector2(x - (width / 2), y - (height / 2));

        //Sprite
        sprite = new Sprite(AssetLoader.enemieshape);
        sprite.setOriginCenter();
        sprite.setPosition(position.x, position.y);
        sprite.setSize(width, height);

        bonusprite = new Sprite(AssetLoader.enemieshape);
        bonusprite.setOriginCenter();
        bonusprite.setPosition(position.x, position.y);
        bonusprite.setSize(width + 10, height + 10);
        bonusprite.setColor(Color.WHITE);

        //TWEENS STUFF
        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();

        //STARTING TWEENS
        enemyState = EnemyState.WAITING;


        velocity = new Vector2(0, 0);
        /*if (position.x <= world.gameWidth / 2) {
            velocity.x -= Math.random() * 40;
        } else {
            velocity.x += Math.random() * 40;
        }*/
        acceleration = new Vector2(0, 0);


        rectangle = new Rectangle(x, y, width, height);
        point = new Vector2(x + width / 2, y + height / 2);
        type = Math.random() < 0.5f ? 1 : 0;
        maxVelocity = (int) (Configuration.MAX_VELOCITY_OF_ENEMIES);

        if (type == 0) {
            sprite.setColor(world.parseColor(Configuration.COLOR_SQUARE_1, 1f));
        } else {
            sprite.setColor(
                    world.parseColor(Configuration.COLOR_SQUARE_2, 1f));
        }

        angleInc = (int) (Math.random() * 2 + 1);
        angleInc *= Math.random() < 0.5f ? -1 : 1;
        scaleValue.setValue(1);
        alpha = 1;
    }


    public void reset(float x, float y) {
        //GENERAL VARIABLES
        this.world = world;

        //Position
        this.position = new Vector2(x - (width / 2), y - (height / 2));

        //Sprite
        sprite = new Sprite(AssetLoader.square);
        sprite.setOriginCenter();
        sprite.setPosition(position.x, position.y);
        sprite.setSize(width, height);

        bonusprite = new Sprite(AssetLoader.square);
        bonusprite.setOriginCenter();
        bonusprite.setPosition(position.x, position.y);
        bonusprite.setSize(width + 10, height + 10);
        bonusprite.setColor(Color.WHITE);

        //TWEENS STUFF
        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();

        //STARTING TWEENS
        enemyState = EnemyState.WAITING;


        velocity = new Vector2(0, 0);
        /*if (position.x <= world.gameWidth / 2) {
            velocity.x -= Math.random() * 40;
        } else {
            velocity.x += Math.random() * 40;
        }*/
        acceleration = new Vector2(0, 0);


        rectangle = new Rectangle(x, y, width, height);
        point = new Vector2(x + width / 2, y + height / 2);
        type = Math.random() < 0.5f ? 1 : 0;
        maxVelocity = (int) (900);

        if (type == 0) {
            sprite.setColor(world.parseColor(Configuration.COLOR_SQUARE_1, 1f));
        } else {
            sprite.setColor(
                    world.parseColor(Configuration.COLOR_SQUARE_2, 1f));
        }

        angleInc = (int) (Math.random() * 2 + 1);
        angleInc *= Math.random() < 0.5f ? -1 : 1;
        scaleValue.setValue(1);
        alpha = 1;
    }

    public void update(float delta) {
        angle += angleInc;
        manager.update(delta);
        if (enemyState == EnemyState.MOVING) {
            velocity.add(acceleration.cpy().scl(delta));
            if (velocity.y < -maxVelocity) {
                velocity.y = -maxVelocity;
            }
        } else if (enemyState == EnemyState.MOVING_TO_CENTER && !velSetted) {
            Vector2 vel = new Vector2(world.getCenter().getPosition().x - position.x,
                    world.getCenter().getPosition().y - position.y);
            velocity.set(vel.nor().x * 300, vel.nor().y * 300);
            velSetted = true;
        } else {
            //velocity.set(0,0);
        }

        position.add(velocity.cpy().scl(delta));

        sprite.setPosition(position.x, position.y);
        rectangle.setPosition(position.x,
                position.y);
        sprite.setRotation(angle);
        sprite.setScale(scaleValue.getValue());
        sprite.setOriginCenter();

        if (bonus) {
            bonusprite.setPosition(position.x - 5, position.y - 5);
            bonusprite.setRotation(angle);
            bonusprite.setOriginCenter();
        }
        outOfBounds();


        //Gdx.app.log("EnemyState", enemyState.toString());
    }

    private void outOfBounds() {
        if (position.y < 0 - 200 || position.y > world.gameHeight + 200) {

            enemyState = EnemyState.DEAD;
            position = new Vector2(0, 0);
            velocity.y = 0;
            type = Math.random() < 0.5f ? 1 : 0;
            if (type == 0) {
                sprite.setColor(world.parseColor(Configuration.COLOR_SQUARE_1, 1f));
            } else {
                sprite.setColor(world.parseColor(Configuration.COLOR_SQUARE_2, 1f));
            }
        }
        if (position.x > world.gameWidth - sprite.getWidth() || position.x < 0) {
            enemyState = EnemyState.DEAD;
            if (position.x < 0) {
                position.x = 0;
            } else {
                position.x = world.gameWidth - sprite.getWidth();
            }
            velocity.x *= -1;
        }
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        if (enemyState == EnemyState.MOVING || enemyState == EnemyState.FINISH || enemyState == EnemyState.CENTER) {
            //effect.draw(batch);
            sprite.setAlpha(alpha);
            sprite.draw(batch);

        }
        if (Configuration.DEBUG) {
            batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            shapeRenderer.end();
            batch.begin();
        }

    }

    public Sprite getSprite() {
        return sprite;
    }


    public Rectangle getRectangle() {
        return rectangle;
    }

    public Vector2 getPoint() {
        return point;
    }

    public void noClick() {
        velocity.x = 0;
    }

    public void setAcceleration(Vector2 acc) {
        this.acceleration = acc;
    }

    public EnemyState getEnemyState() {
        return enemyState;
    }

    public void setPosition(Vector2 pos) {
        this.position = pos;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public void createNew(Vector2 pos) {
        //this.setPosition(pos.cpy());
        this.setAcceleration(new Vector2().cpy());
        this.setVelocity(new Vector2(0, 0).cpy());
        this.setAcceleration(new Vector2(0, -500).cpy());
        this.setEnemyState(Enemy.EnemyState.MOVING);
    }

    public void setEnemyState(EnemyState enemyState) {
        this.enemyState = enemyState;
    }

    public void bounce() {
        if (velocity.y < 0) {

            velocity.y *= -1;
            //velocity.x = (float) (Math.random() * 400);
            velocity.x *= Math.random() < 0.5f ? 1 : -1;
            acceleration.y *= -1;

        }

    }

    public float getAngle2Vecs(Vector2 vec1) {
        //float angle1 = (float) Math.toDegrees(Math.atan2(vec1.y - vec.y, vec.x - vec1.x));
        float angle2 = (float) Math.toDegrees(Math.atan2(vec1.y, vec1.x));
        return angle2 + 90;
    }

    public void setVelsetted(boolean bol) {
        velSetted = bol;
    }

    public void randomType() {
        scaleValue.setValue(1);
        type = Math.random() < 0.5f ? 1 : 0;
        if (type == 0) {
            sprite.setColor(world.parseColor(Configuration.COLOR_SQUARE_1, 1f));
        } else {
            sprite.setColor(
                    world.parseColor(Configuration.COLOR_SQUARE_2, 1f));
        }
    }

    public void waitInCenter() {
        centerValue.setValue(0);
        TweenCallback centerCallback = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                setEnemyState(EnemyState.DEAD);
            }
        };
        Tween.to(centerValue, -1, .1f).target(1).setCallbackTriggers(TweenCallback.COMPLETE)
                .setCallback(centerCallback).start(manager);
    }

    public void dissapear() {
        scaleValue.setValue(1);
        TweenCallback cbDis = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                enemyState = EnemyState.WAITING;
            }
        };
        alpha = 0.4f;
        Tween.to(scaleValue, -1, (float) 0.5f).target(0.0f).setCallback(cbDis)
                .setCallbackTriggers(TweenCallback.COMPLETE).start(
                manager);
    }


    public void dissapearFinish() {
        setVelocity(new Vector2(0, 1000));
        alpha = 0.4f;
        TweenCallback cbDis = new TweenCallback() {

            @Override
            public void onEvent(int type, BaseTween<?> source) {
                enemyState = EnemyState.WAITING;
                setPosition(new Vector2(10,world.worldHeight-50));
            }
        };
        Tween.to(scaleValue, -1, (float) 0.5f).target(0.0f).setCallback(cbDis)
                .setCallbackTriggers(TweenCallback.COMPLETE).start(
                manager);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        alpha = 1;
        this.type = type;
        //bonus = false;
        if (type == 0) {
            sprite.setColor(world.parseColor(Configuration.COLOR_SQUARE_1, 1f));
        } else {
            sprite.setColor(
                    world.parseColor(Configuration.COLOR_SQUARE_2, 1f));
        }
    }

}
