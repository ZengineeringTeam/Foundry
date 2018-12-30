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
    private final int current, max;

    public ElementTemperature(int current, int max)
    {
        this.current = current / 100;
        this.max = max / 100;
    }

    public ElementTemperature(ByteBuf buf)
    {
        this.current = buf.readInt();
        this.max = buf.readInt();
    }

    @Override
    public void render(int x, int y)
    {
        Color color = new Color(255, 170, 0);
        ElementProgressRender.render(new ProgressStyle().filledColor(color.hashCode()).alternateFilledColor(color.darker().hashCode()).showText(false), current - 300, max - 300, x, y, 100, 12);
        ElementTextRender.render(current + " K / " + max + " K", x + 3, y + 2);
    }

    @Override
    public int getWidth()
    {
        return 100;
    }

    @Override
    public int getHeight()
    {
        return 12;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.current);
        buf.writeInt(this.max);
    }

    @Override
    public int getID()
    {
        return ELEMENT_TEMPERATURE;
    }
}
