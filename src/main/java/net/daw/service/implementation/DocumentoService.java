/*
 * Copyright (c) 2015 by Rafael Angel Aznar Aparici (rafaaznar at gmail dot com)
 * 
 * openAUSIAS: The stunning micro-library that helps you to develop easily 
 *             AJAX web applications by using Java and jQuery
 * openAUSIAS is distributed under the MIT License (MIT)
 * Sources at https://github.com/rafaelaznar/openAUSIAS
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.daw.service.implementation;

import com.google.gson.Gson;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import net.daw.bean.implementation.DocumentoBean;
import net.daw.bean.implementation.UsuarioBean;
import net.daw.connection.implementation.BoneConnectionPoolImpl;
import net.daw.connection.publicinterface.ConnectionInterface;
import net.daw.dao.implementation.DocumentoDao;

import net.daw.helper.statics.AppConfigurationHelper;
import static net.daw.helper.statics.AppConfigurationHelper.getSourceConnection;
import net.daw.helper.statics.ExceptionBooster;
import net.daw.helper.statics.FilterBeanHelper;
import net.daw.helper.statics.JsonMessage;
import net.daw.helper.statics.ParameterCook;
import net.daw.service.publicinterface.TableServiceInterface;
import net.daw.service.publicinterface.ViewServiceInterface;

public class DocumentoService implements TableServiceInterface, ViewServiceInterface {

    protected HttpServletRequest oRequest = null;

    public DocumentoService(HttpServletRequest request) {
        oRequest = request;
    }

    private Boolean checkpermission(String strMethodName) throws Exception {
        UsuarioBean oUserBean = (UsuarioBean) oRequest.getSession().getAttribute("userBean");
        if (oUserBean != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getcount() throws Exception {
        if (this.checkpermission("getcount")) {
            String data = null;
            ArrayList<FilterBeanHelper> alFilter = ParameterCook.prepareFilter(oRequest);
            Connection oConnection = null;
            ConnectionInterface oDataConnectionSource = null;
            try {
                oDataConnectionSource = getSourceConnection();
                oConnection = oDataConnectionSource.newConnection();
                DocumentoDao oDocumentoDao = new DocumentoDao(oConnection);
                data = JsonMessage.getJson("200", Integer.toString(oDocumentoDao.getCount(alFilter)));
            } catch (Exception ex) {
                ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getCount ERROR: " + ex.getMessage()));
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (oDataConnectionSource != null) {
                    oDataConnectionSource.disposeConnection();
                }
            }
            return data;
        } else {
            return JsonMessage.getJsonMsg("401", "Unauthorized");
        }
    }

    @Override
    public String get() throws Exception {
        if (this.checkpermission("get")) {
            int id = ParameterCook.prepareId(oRequest);
            String data = null;
            Connection oConnection = null;
            ConnectionInterface oDataConnectionSource = null;
            try {
                oDataConnectionSource = getSourceConnection();
                oConnection = oDataConnectionSource.newConnection();
                DocumentoDao oDocumentoDao = new DocumentoDao(oConnection);
                DocumentoBean oDocumentoBean = new DocumentoBean(id);
                oDocumentoBean = oDocumentoDao.get(oDocumentoBean, AppConfigurationHelper.getJsonDepth());
                Gson gson = AppConfigurationHelper.getGson();
                data = JsonMessage.getJson("200", AppConfigurationHelper.getGson().toJson(oDocumentoBean));
            } catch (Exception ex) {
                ExceptionBooster.boost(new Exception(this.getClass().getName() + ":get ERROR: " + ex.getMessage()));
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (oDataConnectionSource != null) {
                    oDataConnectionSource.disposeConnection();
                }
            }
            return data;

        } else {
            return JsonMessage.getJsonMsg("401", "Unauthorized");
        }
    }

    @Override
    public String getall() throws Exception {
        if (this.checkpermission("getall")) {
            ArrayList<FilterBeanHelper> alFilter = ParameterCook.prepareFilter(oRequest);
            HashMap<String, String> hmOrder = ParameterCook.prepareOrder(oRequest);
            String data = null;
            Connection oConnection = null;
            ConnectionInterface oDataConnectionSource = null;

            try {
                oDataConnectionSource = getSourceConnection();
                oConnection = oDataConnectionSource.newConnection();
                DocumentoDao oDocumentoDao = new DocumentoDao(oConnection);
                ArrayList<DocumentoBean> arrBeans = oDocumentoDao.getAll(alFilter, hmOrder, AppConfigurationHelper.getJsonDepth());
                data = JsonMessage.getJson("200", AppConfigurationHelper.getGson().toJson(arrBeans));
            } catch (Exception ex) {
                ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getAll ERROR: " + ex.getMessage()));
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (oDataConnectionSource != null) {
                    oDataConnectionSource.disposeConnection();
                }
            }
            return data;
        } else {
            return JsonMessage.getJsonMsg("401", "Unauthorized");
        }
    }

    @Override
    public String getpage() throws Exception {
        if (this.checkpermission("getpage")) {
            int intRegsPerPag = ParameterCook.prepareRpp(oRequest);;
            int intPage = ParameterCook.preparePage(oRequest);
            ArrayList<FilterBeanHelper> alFilter = ParameterCook.prepareFilter(oRequest);
            HashMap<String, String> hmOrder = ParameterCook.prepareOrder(oRequest);
            String data = null;
            Connection oConnection = null;
            ConnectionInterface oDataConnectionSource = null;
            try {
                oDataConnectionSource = getSourceConnection();
                oConnection = oDataConnectionSource.newConnection();
                DocumentoDao oDocumentoDao = new DocumentoDao(oConnection);
                List<DocumentoBean> arrBeans = oDocumentoDao.getPage(intRegsPerPag, intPage, alFilter, hmOrder, AppConfigurationHelper.getJsonDepth());
                data = JsonMessage.getJson("200", AppConfigurationHelper.getGson().toJson(arrBeans));
            } catch (Exception ex) {
                ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getPage ERROR: " + ex.getMessage()));
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (oDataConnectionSource != null) {
                    oDataConnectionSource.disposeConnection();
                }
            }
            return data;
        } else {
            return JsonMessage.getJsonMsg("401", "Unauthorized");
        }
    }

    @Override
    public String getpages() throws Exception {
        if (this.checkpermission("getpages")) {
            int intRegsPerPag = ParameterCook.prepareRpp(oRequest);
            ArrayList<FilterBeanHelper> alFilter = ParameterCook.prepareFilter(oRequest);
            String data = null;
            Connection oConnection = null;
            ConnectionInterface oDataConnectionSource = null;
            try {
                oDataConnectionSource = getSourceConnection();
                oConnection = oDataConnectionSource.newConnection();
                DocumentoDao oDocumentoDao = new DocumentoDao(oConnection);
                data = JsonMessage.getJson("200", Integer.toString(oDocumentoDao.getPages(intRegsPerPag, alFilter)));
            } catch (Exception ex) {
                ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getPages ERROR: " + ex.getMessage()));
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (oDataConnectionSource != null) {
                    oDataConnectionSource.disposeConnection();
                }
            }
            return data;
        } else {
            return JsonMessage.getJsonMsg("401", "Unauthorized");
        }
    }

    @Override
    public String getaggregateviewsome() throws Exception {
        if (this.checkpermission("getaggregateviewsome")) {
            String data = null;
            try {
                String page = this.getpage();
                String pages = this.getpages();
                String registers = this.getcount();
                data = "{"
                        + "\"page\":" + page
                        + ",\"pages\":" + pages
                        + ",\"registers\":" + registers
                        + "}";
                data = JsonMessage.getJson("200", data);
            } catch (Exception ex) {
                ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getAggregateViewSome ERROR: " + ex.getMessage()));
            }
            return data;
        } else {
            return JsonMessage.getJsonMsg("401", "Unauthorized");
        }
    }

    @Override
    public String remove() throws Exception {
        if (this.checkpermission("remove")) {
            Integer id = ParameterCook.prepareId(oRequest);
            String resultado = null;
            Connection oConnection = null;
            ConnectionInterface oDataConnectionSource = null;
            try {
                oDataConnectionSource = getSourceConnection();
                oConnection = oDataConnectionSource.newConnection();
                oConnection.setAutoCommit(false);
                DocumentoDao oDocumentoDao = new DocumentoDao(oConnection);
                resultado = JsonMessage.getJson("200", (String) oDocumentoDao.remove(id).toString());
                oConnection.commit();
            } catch (Exception ex) {
                oConnection.rollback();
                ExceptionBooster.boost(new Exception(this.getClass().getName() + ":remove ERROR: " + ex.getMessage()));
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (oDataConnectionSource != null) {
                    oDataConnectionSource.disposeConnection();
                }
            }
            return resultado;
        } else {
            return JsonMessage.getJsonMsg("401", "Unauthorized");
        }
    }

    @Override
    public String set() throws Exception {
        if (this.checkpermission("set")) {
            String jason = ParameterCook.prepareJson(oRequest);
            String resultado = null;
            Connection oConnection = null;
            ConnectionInterface oDataConnectionSource = null;
            try {
                oDataConnectionSource = getSourceConnection();
                oConnection = oDataConnectionSource.newConnection();
                oConnection.setAutoCommit(false);
                DocumentoDao oDocumentoDao = new DocumentoDao(oConnection);
                DocumentoBean oDocumentoBean = new DocumentoBean();
                oDocumentoBean = AppConfigurationHelper.getGson().fromJson(jason, oDocumentoBean.getClass());
                if (oDocumentoBean != null) {
                    Integer iResult = oDocumentoDao.set(oDocumentoBean);
                    if (iResult >= 1) {
                        resultado = JsonMessage.getJson("200", iResult.toString());
                    } else {
                        resultado = JsonMessage.getJson("500", "Error during registry set");
                    }
                } else {
                    resultado = JsonMessage.getJson("500", "Error during registry set");
                }
                oConnection.commit();
            } catch (Exception ex) {
                oConnection.rollback();
                ExceptionBooster.boost(new Exception(this.getClass().getName() + ":set ERROR: " + ex.getMessage()));
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (oDataConnectionSource != null) {
                    oDataConnectionSource.disposeConnection();
                }
            }
            return resultado;
        } else {
            return JsonMessage.getJsonMsg("401", "Unauthorized");
        }
    }

    public String getcontenido(Integer id) throws Exception {
        String data;
        Connection oConnection = null;
        DocumentoBean oDocumentoBean;
        try {
            oConnection = new BoneConnectionPoolImpl().newConnection();
            oDocumentoBean = new DocumentoBean(id);
            DocumentoDao oDocumentoDao = new DocumentoDao(oConnection);
            oDocumentoBean = oDocumentoDao.get(oDocumentoBean, AppConfigurationHelper.getJsonDepth());
        } catch (Exception e) {
            throw new ServletException(this.getClass().getName() + ":GetContenido: Error: " + e.getMessage());
        }
        oConnection.close();
        return "{\"data\":\"" + oDocumentoBean.getContenido() + "\"}";
    }

}
