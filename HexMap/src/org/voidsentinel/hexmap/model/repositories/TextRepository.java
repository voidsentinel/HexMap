/**
 * 
 */
package org.voidsentinel.hexmap.model.repositories;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ConfigurationDecoder;
import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apache.commons.configuration2.convert.ConversionHandler;
import org.apache.commons.configuration2.convert.ListDelimiterHandler;
import org.apache.commons.configuration2.event.BaseEventSource;
import org.apache.commons.configuration2.event.ConfigurationErrorEvent;
import org.apache.commons.configuration2.event.Event;
import org.apache.commons.configuration2.event.EventListener;
import org.apache.commons.configuration2.event.EventListenerRegistrationData;
import org.apache.commons.configuration2.event.EventType;
import org.apache.commons.configuration2.interpol.ConfigurationInterpolator;
import org.apache.commons.configuration2.interpol.Lookup;
import org.apache.commons.configuration2.io.ConfigurationLogger;
import org.apache.commons.configuration2.sync.LockMode;
import org.apache.commons.configuration2.sync.Synchronizer;

/**
 * @author VoidSentinel
 *
 */
public class TextRepository {

	private final static CompositeConfiguration properties = new CompositeConfiguration();

	/**
	 * @param eventType
	 * @return
	 * @see org.apache.commons.configuration2.event.BaseEventSource#getEventListeners(org.apache.commons.configuration2.event.EventType)
	 */
	public static <T extends Event> Collection<EventListener<? super T>> getEventListeners(EventType<T> eventType) {
		return properties.getEventListeners(eventType);
	}

	/**
	 * @return
	 * @see org.apache.commons.configuration2.event.BaseEventSource#getEventListenerRegistrations()
	 */
	public static List<EventListenerRegistrationData<?>> getEventListenerRegistrations() {
		return properties.getEventListenerRegistrations();
	}

	/**
	 * @param eventType
	 * @param listener
	 * @see org.apache.commons.configuration2.event.BaseEventSource#addEventListener(org.apache.commons.configuration2.event.EventType, org.apache.commons.configuration2.event.EventListener)
	 */
	public static <T extends Event> void addEventListener(EventType<T> eventType, EventListener<? super T> listener) {
		properties.addEventListener(eventType, listener);
	}

	/**
	 * @param config
	 * @see org.apache.commons.configuration2.CompositeConfiguration#addConfiguration(org.apache.commons.configuration2.Configuration)
	 */
	public static void addConfiguration(Configuration config) {
		properties.addConfiguration(config);
	}

	/**
	 * 
	 * @see org.apache.commons.configuration2.event.BaseEventSource#clearEventListeners()
	 */
	public static void clearEventListeners() {
		properties.clearEventListeners();
	}

	/**
	 * @param config
	 * @param asInMemory
	 * @see org.apache.commons.configuration2.CompositeConfiguration#addConfiguration(org.apache.commons.configuration2.Configuration, boolean)
	 */
	public static void addConfiguration(Configuration config, boolean asInMemory) {
		properties.addConfiguration(config, asInMemory);
	}

	/**
	 * 
	 * @see org.apache.commons.configuration2.event.BaseEventSource#clearErrorListeners()
	 */
	public static void clearErrorListeners() {
		properties.clearErrorListeners();
	}

	/**
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getListDelimiterHandler()
	 */
	public static ListDelimiterHandler getListDelimiterHandler() {
		return properties.getListDelimiterHandler();
	}

	/**
	 * @param source
	 * @see org.apache.commons.configuration2.event.BaseEventSource#copyEventListeners(org.apache.commons.configuration2.event.BaseEventSource)
	 */
	public static void copyEventListeners(BaseEventSource source) {
		properties.copyEventListeners(source);
	}

	/**
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getConversionHandler()
	 */
	public static ConversionHandler getConversionHandler() {
		return properties.getConversionHandler();
	}

	/**
	 * @param config
	 * @see org.apache.commons.configuration2.CompositeConfiguration#addConfigurationFirst(org.apache.commons.configuration2.Configuration)
	 */
	public static void addConfigurationFirst(Configuration config) {
		properties.addConfigurationFirst(config);
	}

	/**
	 * @param config
	 * @param asInMemory
	 * @see org.apache.commons.configuration2.CompositeConfiguration#addConfigurationFirst(org.apache.commons.configuration2.Configuration, boolean)
	 */
	public static void addConfigurationFirst(Configuration config, boolean asInMemory) {
		properties.addConfigurationFirst(config, asInMemory);
	}

	/**
	 * @param eventType
	 * @param operationType
	 * @param propertyName
	 * @param propertyValue
	 * @param cause
	 * @see org.apache.commons.configuration2.event.BaseEventSource#fireError(org.apache.commons.configuration2.event.EventType, org.apache.commons.configuration2.event.EventType, java.lang.String, java.lang.Object, java.lang.Throwable)
	 */
	public static <T extends ConfigurationErrorEvent> void fireError(EventType<T> eventType, EventType<?> operationType,
	      String propertyName, Object propertyValue, Throwable cause) {
		properties.fireError(eventType, operationType, propertyName, propertyValue, cause);
	}

	/**
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getInterpolator()
	 */
	public static ConfigurationInterpolator getInterpolator() {
		return properties.getInterpolator();
	}

	/**
	 * @return
	 * @see org.apache.commons.configuration2.CompositeConfiguration#getNumberOfConfigurations()
	 */
	public static int getNumberOfConfigurations() {
		return properties.getNumberOfConfigurations();
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.CompositeConfiguration#getList(java.lang.String, java.util.List)
	 */
	public static List<Object> getList(String key, List<?> defaultValue) {
		return properties.getList(key, defaultValue);
	}

	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.configuration2.CompositeConfiguration#getStringArray(java.lang.String)
	 */
	public static String[] getStringArray(String key) {
		return properties.getStringArray(key);
	}

	/**
	 * @param index
	 * @return
	 * @see org.apache.commons.configuration2.CompositeConfiguration#getConfiguration(int)
	 */
	public static Configuration getConfiguration(int index) {
		return properties.getConfiguration(index);
	}

	/**
	 * @return
	 * @see org.apache.commons.configuration2.CompositeConfiguration#getInMemoryConfiguration()
	 */
	public static Configuration getInMemoryConfiguration() {
		return properties.getInMemoryConfiguration();
	}

	/**
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getConfigurationDecoder()
	 */
	public static ConfigurationDecoder getConfigurationDecoder() {
		return properties.getConfigurationDecoder();
	}


	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.configuration2.CompositeConfiguration#getSource(java.lang.String)
	 */
	public static Configuration getSource(String key) {
		return properties.getSource(key);
	}

	/**
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getLogger()
	 */
	public static ConfigurationLogger getLogger() {
		return properties.getLogger();
	}

	/**
	 * 
	 * @see org.apache.commons.configuration2.AbstractConfiguration#addErrorLogListener()
	 */
	public static final void addErrorLogListener() {
		properties.addErrorLogListener();
	}

	/**
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getSynchronizer()
	 */
	public static final Synchronizer getSynchronizer() {
		return properties.getSynchronizer();
	}

	/**
	 * @param key
	 * @param value
	 * @see org.apache.commons.configuration2.AbstractConfiguration#addProperty(java.lang.String, java.lang.Object)
	 */
	public static final void addProperty(String key, Object value) {
		properties.addProperty(key, value);
	}

	/**
	 * @param key
	 * @see org.apache.commons.configuration2.AbstractConfiguration#clearProperty(java.lang.String)
	 */
	public static final void clearProperty(String key) {
		properties.clearProperty(key);
	}

	/**
	 * 
	 * @see org.apache.commons.configuration2.AbstractConfiguration#clear()
	 */
	public static final void clear() {
		properties.clear();
	}

	/**
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getKeys()
	 */
	public static final Iterator<String> getKeys() {
		return properties.getKeys();
	}

	/**
	 * @param prefix
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getKeys(java.lang.String)
	 */
	public static final Iterator<String> getKeys(String prefix) {
		return properties.getKeys(prefix);
	}

	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getProperty(java.lang.String)
	 */
	public static final Object getProperty(String key) {
		return properties.getProperty(key);
	}

	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#containsKey(java.lang.String)
	 */
	public static final boolean containsKey(String key) {
		return properties.containsKey(key);
	}

	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getProperties(java.lang.String)
	 */
	public static Properties getProperties(String key) {
		return properties.getProperties(key);
	}

	/**
	 * @param key
	 * @param defaults
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getProperties(java.lang.String, java.util.Properties)
	 */
	public static Properties getProperties(String key, Properties defaults) {
		return properties.getProperties(key, defaults);
	}

	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getBoolean(java.lang.String)
	 */
	public static boolean getBoolean(String key) {
		return properties.getBoolean(key);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getBoolean(java.lang.String, boolean)
	 */
	public static boolean getBoolean(String key, boolean defaultValue) {
		return properties.getBoolean(key, defaultValue);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getBoolean(java.lang.String, java.lang.Boolean)
	 */
	public static Boolean getBoolean(String key, Boolean defaultValue) {
		return properties.getBoolean(key, defaultValue);
	}

	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getByte(java.lang.String)
	 */
	public static byte getByte(String key) {
		return properties.getByte(key);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getByte(java.lang.String, byte)
	 */
	public static byte getByte(String key, byte defaultValue) {
		return properties.getByte(key, defaultValue);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getByte(java.lang.String, java.lang.Byte)
	 */
	public static Byte getByte(String key, Byte defaultValue) {
		return properties.getByte(key, defaultValue);
	}

	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getDouble(java.lang.String)
	 */
	public static double getDouble(String key) {
		return properties.getDouble(key);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getDouble(java.lang.String, double)
	 */
	public static double getDouble(String key, double defaultValue) {
		return properties.getDouble(key, defaultValue);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getDouble(java.lang.String, java.lang.Double)
	 */
	public static Double getDouble(String key, Double defaultValue) {
		return properties.getDouble(key, defaultValue);
	}

	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getFloat(java.lang.String)
	 */
	public static float getFloat(String key) {
		return properties.getFloat(key);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getFloat(java.lang.String, float)
	 */
	public static float getFloat(String key, float defaultValue) {
		return properties.getFloat(key, defaultValue);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getFloat(java.lang.String, java.lang.Float)
	 */
	public static Float getFloat(String key, Float defaultValue) {
		return properties.getFloat(key, defaultValue);
	}

	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getInt(java.lang.String)
	 */
	public static int getInt(String key) {
		return properties.getInt(key);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getInt(java.lang.String, int)
	 */
	public static int getInt(String key, int defaultValue) {
		return properties.getInt(key, defaultValue);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getInteger(java.lang.String, java.lang.Integer)
	 */
	public static Integer getInteger(String key, Integer defaultValue) {
		return properties.getInteger(key, defaultValue);
	}

	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getLong(java.lang.String)
	 */
	public static long getLong(String key) {
		return properties.getLong(key);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getLong(java.lang.String, long)
	 */
	public static long getLong(String key, long defaultValue) {
		return properties.getLong(key, defaultValue);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getLong(java.lang.String, java.lang.Long)
	 */
	public static Long getLong(String key, Long defaultValue) {
		return properties.getLong(key, defaultValue);
	}

	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getShort(java.lang.String)
	 */
	public static short getShort(String key) {
		return properties.getShort(key);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getShort(java.lang.String, short)
	 */
	public static short getShort(String key, short defaultValue) {
		return properties.getShort(key, defaultValue);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getShort(java.lang.String, java.lang.Short)
	 */
	public static Short getShort(String key, Short defaultValue) {
		return properties.getShort(key, defaultValue);
	}

	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getBigDecimal(java.lang.String)
	 */
	public static BigDecimal getBigDecimal(String key) {
		return properties.getBigDecimal(key);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getBigDecimal(java.lang.String, java.math.BigDecimal)
	 */
	public static BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		return properties.getBigDecimal(key, defaultValue);
	}

	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getBigInteger(java.lang.String)
	 */
	public static BigInteger getBigInteger(String key) {
		return properties.getBigInteger(key);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getBigInteger(java.lang.String, java.math.BigInteger)
	 */
	public static BigInteger getBigInteger(String key, BigInteger defaultValue) {
		return properties.getBigInteger(key, defaultValue);
	}

	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getString(java.lang.String)
	 */
	public static String getString(String key) {
		return properties.getString(key);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getString(java.lang.String, java.lang.String)
	 */
	public static String getString(String key, String defaultValue) {
		return properties.getString(key, defaultValue);
	}

	/**
	 * @param key
	 * @param decoder
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getEncodedString(java.lang.String, org.apache.commons.configuration2.ConfigurationDecoder)
	 */
	public static String getEncodedString(String key, ConfigurationDecoder decoder) {
		return properties.getEncodedString(key, decoder);
	}

	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getEncodedString(java.lang.String)
	 */
	public static String getEncodedString(String key) {
		return properties.getEncodedString(key);
	}

	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getList(java.lang.String)
	 */
	public static List<Object> getList(String key) {
		return properties.getList(key);
	}

	/**
	 * @param cls
	 * @param key
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#get(java.lang.Class, java.lang.String)
	 */
	public static <T> T get(Class<T> cls, String key) {
		return properties.get(cls, key);
	}

	/**
	 * @param cls
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#get(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	public static <T> T get(Class<T> cls, String key, T defaultValue) {
		return properties.get(cls, key, defaultValue);
	}

	/**
	 * @param cls
	 * @param key
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getArray(java.lang.Class, java.lang.String)
	 */
	public static Object getArray(Class<?> cls, String key) {
		return properties.getArray(cls, key);
	}

	/**
	 * @param cls
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getArray(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	public static Object getArray(Class<?> cls, String key, Object defaultValue) {
		return properties.getArray(cls, key, defaultValue);
	}

	/**
	 * @param cls
	 * @param key
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getList(java.lang.Class, java.lang.String)
	 */
	public static <T> List<T> getList(Class<T> cls, String key) {
		return properties.getList(cls, key);
	}

	/**
	 * @param cls
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getList(java.lang.Class, java.lang.String, java.util.List)
	 */
	public static <T> List<T> getList(Class<T> cls, String key, List<T> defaultValue) {
		return properties.getList(cls, key, defaultValue);
	}

	/**
	 * @param cls
	 * @param key
	 * @param target
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getCollection(java.lang.Class, java.lang.String, java.util.Collection)
	 */
	public static <T> Collection<T> getCollection(Class<T> cls, String key, Collection<T> target) {
		return properties.getCollection(cls, key, target);
	}

	/**
	 * @param cls
	 * @param key
	 * @param target
	 * @param defaultValue
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#getCollection(java.lang.Class, java.lang.String, java.util.Collection, java.util.Collection)
	 */
	public static <T> Collection<T> getCollection(Class<T> cls, String key, Collection<T> target, Collection<T> defaultValue) {
		return properties.getCollection(cls, key, target, defaultValue);
	}

	/**
	 * @param c
	 * @see org.apache.commons.configuration2.AbstractConfiguration#copy(org.apache.commons.configuration2.Configuration)
	 */
	public static void copy(Configuration c) {
		properties.copy(c);
	}


	/**
	 * @return
	 * @see org.apache.commons.configuration2.event.BaseEventSource#isDetailEvents()
	 */
	public static boolean isDetailEvents() {
		return properties.isDetailEvents();
	}

	/**
	 * @param enable
	 * @see org.apache.commons.configuration2.event.BaseEventSource#setDetailEvents(boolean)
	 */
	public static void setDetailEvents(boolean enable) {
		properties.setDetailEvents(enable);
	}

	/**
	 * @param eventType
	 * @param listener
	 * @return
	 * @see org.apache.commons.configuration2.event.BaseEventSource#removeEventListener(org.apache.commons.configuration2.event.EventType, org.apache.commons.configuration2.event.EventListener)
	 */
	public static <T extends Event> boolean removeEventListener(EventType<T> eventType, EventListener<? super T> listener) {
		return properties.removeEventListener(eventType, listener);
	}

	/**
	 * @param conversionHandler
	 * @see org.apache.commons.configuration2.AbstractConfiguration#setConversionHandler(org.apache.commons.configuration2.convert.ConversionHandler)
	 */
	public static void setConversionHandler(ConversionHandler conversionHandler) {
		properties.setConversionHandler(conversionHandler);
	}

	/**
	 * @param throwExceptionOnMissing
	 * @see org.apache.commons.configuration2.AbstractConfiguration#setThrowExceptionOnMissing(boolean)
	 */
	public static void setThrowExceptionOnMissing(boolean throwExceptionOnMissing) {
		properties.setThrowExceptionOnMissing(throwExceptionOnMissing);
	}

	/**
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#isThrowExceptionOnMissing()
	 */
	public static boolean isThrowExceptionOnMissing() {
		return properties.isThrowExceptionOnMissing();
	}

	/**
	 * @param config
	 * @see org.apache.commons.configuration2.CompositeConfiguration#removeConfiguration(org.apache.commons.configuration2.Configuration)
	 */
	public static void removeConfiguration(Configuration config) {
		properties.removeConfiguration(config);
	}

	/**
	 * @param ci
	 * @see org.apache.commons.configuration2.AbstractConfiguration#setInterpolator(org.apache.commons.configuration2.interpol.ConfigurationInterpolator)
	 */
	public static final void setInterpolator(ConfigurationInterpolator ci) {
		properties.setInterpolator(ci);
	}

	/**
	 * @param prefixLookups
	 * @param defLookups
	 * @see org.apache.commons.configuration2.AbstractConfiguration#installInterpolator(java.util.Map, java.util.Collection)
	 */
	public static final void installInterpolator(Map<String, ? extends Lookup> prefixLookups,
	      Collection<? extends Lookup> defLookups) {
		properties.installInterpolator(prefixLookups, defLookups);
	}

	/**
	 * @param lookups
	 * @see org.apache.commons.configuration2.AbstractConfiguration#setPrefixLookups(java.util.Map)
	 */
	public static void setPrefixLookups(Map<String, ? extends Lookup> lookups) {
		properties.setPrefixLookups(lookups);
	}

	/**
	 * @param lookups
	 * @see org.apache.commons.configuration2.AbstractConfiguration#setDefaultLookups(java.util.Collection)
	 */
	public static void setDefaultLookups(Collection<? extends Lookup> lookups) {
		properties.setDefaultLookups(lookups);
	}

	/**
	 * @param parent
	 * @see org.apache.commons.configuration2.AbstractConfiguration#setParentInterpolator(org.apache.commons.configuration2.interpol.ConfigurationInterpolator)
	 */
	public static void setParentInterpolator(ConfigurationInterpolator parent) {
		properties.setParentInterpolator(parent);
	}

	/**
	 * @param configurationDecoder
	 * @see org.apache.commons.configuration2.AbstractConfiguration#setConfigurationDecoder(org.apache.commons.configuration2.ConfigurationDecoder)
	 */
	public static void setConfigurationDecoder(ConfigurationDecoder configurationDecoder) {
		properties.setConfigurationDecoder(configurationDecoder);
	}

	/**
	 * @param listDelimiterHandler
	 * @see org.apache.commons.configuration2.CompositeConfiguration#setListDelimiterHandler(org.apache.commons.configuration2.convert.ListDelimiterHandler)
	 */
	public static void setListDelimiterHandler(ListDelimiterHandler listDelimiterHandler) {
		properties.setListDelimiterHandler(listDelimiterHandler);
	}

	/**
	 * @param log
	 * @see org.apache.commons.configuration2.AbstractConfiguration#setLogger(org.apache.commons.configuration2.io.ConfigurationLogger)
	 */
	public static void setLogger(ConfigurationLogger log) {
		properties.setLogger(log);
	}

	/**
	 * @param synchronizer
	 * @see org.apache.commons.configuration2.AbstractConfiguration#setSynchronizer(org.apache.commons.configuration2.sync.Synchronizer)
	 */
	public static final void setSynchronizer(Synchronizer synchronizer) {
		properties.setSynchronizer(synchronizer);
	}

	/**
	 * @param mode
	 * @see org.apache.commons.configuration2.AbstractConfiguration#lock(org.apache.commons.configuration2.sync.LockMode)
	 */
	public static final void lock(LockMode mode) {
		properties.lock(mode);
	}

	/**
	 * @param mode
	 * @see org.apache.commons.configuration2.AbstractConfiguration#unlock(org.apache.commons.configuration2.sync.LockMode)
	 */
	public static final void unlock(LockMode mode) {
		properties.unlock(mode);
	}

	/**
	 * @param prefix
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#subset(java.lang.String)
	 */
	public static Configuration subset(String prefix) {
		return properties.subset(prefix);
	}

	/**
	 * @param prefix
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#immutableSubset(java.lang.String)
	 */
	public static ImmutableConfiguration immutableSubset(String prefix) {
		return properties.immutableSubset(prefix);
	}

	/**
	 * @param key
	 * @param value
	 * @see org.apache.commons.configuration2.AbstractConfiguration#setProperty(java.lang.String, java.lang.Object)
	 */
	public static final void setProperty(String key, Object value) {
		properties.setProperty(key, value);
	}

	/**
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#isEmpty()
	 */
	public static final boolean isEmpty() {
		return properties.isEmpty();
	}

	/**
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#size()
	 */
	public static final int size() {
		return properties.size();
	}

	/**
	 * @param c
	 * @see org.apache.commons.configuration2.AbstractConfiguration#append(org.apache.commons.configuration2.Configuration)
	 */
	public static void append(Configuration c) {
		properties.append(c);
	}

	/**
	 * @return
	 * @see org.apache.commons.configuration2.AbstractConfiguration#interpolatedConfiguration()
	 */
	public static Configuration interpolatedConfiguration() {
		return properties.interpolatedConfiguration();
	}


	
}
