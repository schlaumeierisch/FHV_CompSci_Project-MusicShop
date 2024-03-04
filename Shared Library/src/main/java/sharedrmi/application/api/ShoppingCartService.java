package sharedrmi.application.api;

import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.dto.CartLineItemDTO;
import sharedrmi.application.dto.ShoppingCartDTO;
import sharedrmi.application.dto.SongDTO;

import javax.ejb.Remote;
import javax.naming.NoPermissionException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Remote
public interface ShoppingCartService extends Serializable {

    ShoppingCartDTO getCart() throws NoPermissionException;

    void addAlbumsToCart(AlbumDTO album, int amount) throws NoPermissionException;

    void addSongsToCart(List<SongDTO> songs) throws NoPermissionException;

    void changeQuantity(CartLineItemDTO cartLineItemDTO, int quantity) throws NoPermissionException;

    void removeLineItemFromCart(CartLineItemDTO cartLineItemDTO) throws NoPermissionException;

    void clearCart() throws NoPermissionException;

    void buyShoppingCart(String ownerId) throws IOException;

}
