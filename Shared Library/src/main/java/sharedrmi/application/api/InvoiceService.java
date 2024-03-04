package sharedrmi.application.api;

import sharedrmi.application.dto.InvoiceDTO;
import sharedrmi.application.dto.InvoiceLineItemDTO;
import sharedrmi.application.exceptions.AlbumNotFoundException;
import sharedrmi.application.exceptions.InvoiceNotFoundException;
import sharedrmi.application.exceptions.NotEnoughStockException;
import sharedrmi.domain.valueobjects.InvoiceId;

import javax.ejb.Remote;
import javax.naming.NoPermissionException;
import java.io.Serializable;

@Remote
public interface InvoiceService extends Serializable {

    InvoiceDTO findInvoiceById(InvoiceId invoiceId) throws NoPermissionException, InvoiceNotFoundException;

    InvoiceId createInvoice(InvoiceDTO invoiceDTO) throws NoPermissionException, NotEnoughStockException, AlbumNotFoundException;

    void returnInvoiceLineItem(InvoiceId invoiceId, InvoiceLineItemDTO invoiceLineItemDTO, int returnQuantity) throws NoPermissionException, InvoiceNotFoundException;

}