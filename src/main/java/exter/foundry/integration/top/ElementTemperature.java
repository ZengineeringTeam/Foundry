package exter.foundry.integration.top;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.client.ElementProgressRender;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;

import java.awt.*;

public class ElementTemperature implements IElement
{
    private static int ELEMENT_TEMPERATURE = TheOneProbe.theOneProbeImp.registerElementFactory(ElementTemperature::new);
    private final int current, max, color1, color2;

    public ElementTemperature(int current, int max, int color1) {
        this.current = current;
        this.max = max;
        this.color1 = color1;
        this.color2 = new Color(this.color1).darker().hashCode();
    }

    public ElementTemperature(ByteBuf buf) {
        this.current = buf.readInt();
        this.max = buf.readInt();
        this.color1 = buf.readInt();
        this.color2 = new Color(this.color1).darker().hashCode();
    }

    @Override
    public void render(int x, int y) {
        ElementProgressRender.render(new ProgressStyle().filledColor(color1).alternateFilledColor(color2).showText(false), current - 300, max - 300, x, y, 100, 12);
        ElementTextRender.render(current + " K / " + max + " K", x + 3, y + 2);
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return 12;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.current);
        buf.writeInt(this.max);
        buf.writeInt(this.color1);
    }

    @Override
    public int getID() {
        return ELEMENT_TEMPERATURE;
    }
}
