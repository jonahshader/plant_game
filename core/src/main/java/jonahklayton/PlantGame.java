package jonahklayton;

import com.badlogic.gdx.Game;
import jonahklayton.screens.FirstScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class PlantGame extends Game {
	@Override
	public void create() {
		setScreen(new FirstScreen());
	}
}