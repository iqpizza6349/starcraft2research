package io.iqpizza.starcraft.debug;

import com.github.ocraft.s2client.bot.gateway.DebugInterface;
import com.github.ocraft.s2client.protocol.debug.Color;
import com.github.ocraft.s2client.protocol.spatial.Point;
import io.iqpizza.starcraft.simcity.GameMap;
import io.iqpizza.starcraft.simcity.TileType;

public final class GameDebugDrawer {
    private GameDebugDrawer() {
        throw new AssertionError();
    }

    public static void drawResources(DebugInterface debug, GameMap gameMap) {
        TileType[][] tiles = gameMap.getTiles();
        int width = gameMap.getFullWidth();
        int height = gameMap.getFullHeight();

        // 맵의 모든 타일을 순회하여 미네랄과 가스 위치를 찾음
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (tiles[y][x] == TileType.MINERAL) {
                    // 미네랄 필드가 있는 위치에 파란색 원 그리기
                    drawCircle(debug, x, y, Color.BLUE);
                } else if (tiles[y][x] == TileType.GAS) {
                    // 가스 간헐천이 있는 위치에 초록색 원 그리기
                    drawCircle(debug, x, y, Color.GREEN);
                }
            }
        }

        // 디버그 정보를 화면에 적용
        debug.sendDebug();
    }

    private static void drawCircle(DebugInterface debug, int x, int y, Color color) {
        // z 좌표를 고정값(예: 12)으로 설정하여 2D 좌표를 3D 공간에 매핑
        debug.debugSphereOut(Point.of(x, y, 12), 1.0f, color);
    }
}
