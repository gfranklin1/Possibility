package Levels;

import Media.AnimationLoader;
import Media.ImageLoader;
import core.Cooldown;
import core.Game;
import core.Main;
import GameObject.Block.Border;
import GameObject.Block.Door;
import GameObject.Block.Switch;
import GameObject.Block.SwitchManager;
import GameObject.Block.Tile;
import GameObject.Block.TileManager;
import GameObject.Block.pushBlock;
import GameObject.Block.pushBlockManager;
import GameObject.Entity.Hazard.Stationary.Spike;
import GameObject.Entity.Hazard.Stationary.SpikeManager;
import GameObject.Entity.Interactable.Key;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Level6 extends Level {
	private int doorTimer = 0;
	private static boolean isDoorOpen = false, keyGrabbed = false, isCompleted = false;
	private static float pushx = 250, pushy = Main.getScreenHeight() - 210;
	private static float platformX, platformV, elevatorY, elevatorVy, elevatorX, elevatorVx;

	public Level6() {
		platformX = 1900;
	}

	public static void reset() {
		isDoorOpen = false;
		keyGrabbed = false;
		platformX = 1900;
		platformV = 5;
		elevatorY = Main.getScreenHeight() - 120;
		elevatorVy = 5;
		elevatorX = 3000;
		elevatorVx = 5;
		pushx = 250;
		pushy = Main.getScreenHeight() - 210;
	}

	public void render(Graphics g) throws SlickException {
		g.setColor(Color.white);

		for (Tile t : TileManager.getWhiteTiles()) {
			t.render(g);
		}

		g.setColor(Color.black);

		for (Tile t : TileManager.getBlackTiles()) {
			t.render(g);
		}

		for (Spike s : SpikeManager.getSpikes()) {
			s.render(g);
		}

		if (!keyGrabbed) {
			AnimationLoader.returnAnimation("key").draw(2000, Main.getScreenHeight() / 2, 180, 180);
		}

		if (keyGrabbed) {
			if (doorTimer < 25) {
				AnimationLoader.returnAnimation("door").draw(Main.getScreenWidth() * 2,
						Main.getScreenHeight() / 2, 150, 230);
			}
			if (doorTimer >= 25) {
				ImageLoader.returnImages("openDoor").draw(Main.getScreenWidth() * 2,
						Main.getScreenHeight() / 2, 150, 230);
			}
		} else {
			ImageLoader.returnImages("door").draw(Main.getScreenWidth() * 2,
					Main.getScreenHeight() / 2, 150, 230);
		}

		for (Switch s : SwitchManager.getSwitches()) {
			s.render(g);
		}

		for (pushBlock pB : pushBlockManager.getPushBlocks()) {
			pB.render(g);
		}
		Cooldown.draw(g);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		TileManager.clearTiles();
		Border.clearBorders();
		SwitchManager.clearSwitches();
		SpikeManager.clearSpikes();
		pushBlockManager.clearPushBlocks();

		if (Game.getP().getPlayerShape().getCenterX() >= elevatorX
				&& Game.getP().getPlayerShape().getCenterX() <= elevatorX + 100
				&& Game.getP().getPlayerShape().getCenterY() <= elevatorY) {
			if (elevatorX <= 3400 && elevatorY > 300) {
				elevatorY -= elevatorVy;
			}

			if (elevatorY <= 300 && elevatorX < 3400) {
				elevatorVy = 0;
				elevatorX += elevatorVx;
			}
		}

		platformX += platformV;

		if (platformX >= 2300) {
			platformV = platformV * (-1);
		} else if (platformX <= 1900) {
			platformV = platformV * (-1);
		}

		if (keyGrabbed) {
			doorTimer++;
		}

		generateWorld();

		for (pushBlock pB : pushBlockManager.getPushBlocks()) {
			pB.update();
		}
		for (Switch s : SwitchManager.getSwitches()) {
			if (Game.getP().getxPos() > s.getX() && Game.getP().getxPos() < s.getX() + s.getWidth()
					&& Game.getP().getyPos() + Game.getP().getHeight() > s.getY()
					&& Game.getP().getyPos() < s.getY() + s.getHeight()) {
				Game.switchg();
			}
		}

		if (Game.getP().getPlayerShape().intersects(Door.getDoorHitBox()) && isDoorOpen) {
			isCompleted = true;
			Game.getP().playerReset();
			Game.setCurLevel(Game.getLevelSelect());
			Game.getP().setxPos(150);
			Game.getP().setyPos(Main.getScreenHeight() / 2);
		}

		if (Game.getP().getPlayerShape().intersects(Key.getKeyHitBox())) {
			keyGrabbed = true;
			isDoorOpen = true;
		}

		for (Spike s : SpikeManager.getSpikes()) {
			s.update();
		}

		for (Spike s : SpikeManager.getSpikes()) {
			if (Game.getP().getPlayerShape().intersects(s.getSpike())) {
				Game.getP().die();
			}
		}
	}

	private void generateWorld() {
		TileManager.createTile(2300, Main.getScreenHeight() / 2, 250, 300, "white");
		// Tiles
		TileManager.createBorder(2300, Main.getScreenHeight() / 2, 2550,
				Main.getScreenHeight() / 2);
		TileManager.createBorder(2300, Main.getScreenHeight() / 2 + 300, 2550,
				Main.getScreenHeight() / 2 + 300);
		TileManager.createBorder(2300, Main.getScreenHeight() / 2 + 2, 2300,
				Main.getScreenHeight() / 2 + 300);
		TileManager.createBorder(2550, Main.getScreenHeight() / 2 + 2, 2550,
				Main.getScreenHeight() / 2 + 300);
		SwitchManager.createSwitch(1500, Main.getScreenHeight() - 200, 100, 100, true);
		SwitchManager.createSwitch(1500, 100, 100, 100, false);
		SwitchManager.createSwitch(2200, Main.getScreenHeight() - 200, 100, 100, true);
		SwitchManager.createSwitch(2300, 100, 100, 100, false);
		SpikeManager.createSpike(1700, Main.getScreenHeight() - 450, 50, 350, false);
		SpikeManager.createSpike(1750, Main.getScreenHeight() - 350, 50, 250, false);
		SpikeManager.createSpike(1800, Main.getScreenHeight() - 250, 50, 150, false);
		SpikeManager.createSpike(1700, 100, 50, 375, true);
		SpikeManager.createSpike(1750, 100, 50, 275, true);
		SpikeManager.createSpike(1800, 100, 50, 175, true);
		wallsAroundMap();
		// push blocks
		pushBlockManager.createPushBlock(pushx, pushy, 105, 105);
		pushBlockWalls();
		wallsAroundMap();
		// door & key
		if (!keyGrabbed) {
			Key.createKey(2000, Main.getScreenHeight() / 2, 180, 84);
		}
		Door.createDoor(Main.getScreenWidth() * 2, Main.getScreenHeight() / 2, 150, 230);
	}

	public static boolean getCompleted() {
		return isCompleted;
	}

	public static void setCompleted(boolean isCompleted) {
		Level6.isCompleted = isCompleted;
	}

	public static void setPushx(float x) {
		Level6.pushx = x;
	}

	public static void setPushy(float y) {
		Level6.pushy = y;
	}

	@Override
	public int getWidth() {
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}
}